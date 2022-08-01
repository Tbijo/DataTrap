package com.example.datatrap.www

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.models.*
import com.example.datatrap.models.sync.*
import com.example.datatrap.repositories.*
import com.example.datatrap.repositories.datastore.PrefRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.io.File
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SyncClassViewModel @Inject constructor(
    private val mouseRepository: MouseRepository,
    private val occasionRepository: OccasionRepository,
    private val localityRepository: LocalityRepository,
    private val sessionRepository: SessionRepository,
    private val projectRepository: ProjectRepository,
    private val mouseImageRepository: MouseImageRepository,
    private val occasionImageRepository: OccasionImageRepository,
    private val specieImageRepository: SpecieImageRepository
) : ViewModel() {

    private val dataDone = MutableLiveData<Boolean>()
    val dataSavedLive: LiveData<Boolean> by this::dataDone

    fun saveData(listSyncClass: List<SyncClass>) {
        viewModelScope.launch(Dispatchers.IO) {
            listSyncClass.forEach { syncClass ->
                val projectSync: ProjectSync = syncClass.prj
                // ci projekt je v Databaze
                val project: Project? = async { projectRepository.getProjectByName(projectSync.projectName) }.await()

                var projectId = 0L

                if (project != null) {
                    // ak je mozno Update
                    // TODO Aplikacia prinesie vzdy vacsie cislo
                    // nechat tuto starost na nich pred pouzitim vzdy syncronizovat
                    // TODO ak bude mensie cislo tak nemenit hodnotu v stlpci
                    // lebo aj tak kazde pridanie bude hodnotu samo zvacsovat
                    projectId = project.projectId
                } else {
                    // ak nie je tak Insert
                    val newProject: Project = setNewProject(projectSync)
                    projectId = async { projectRepository.insertSyncProject(newProject) }.await()
                }
                // v oboch pripadoch vybrat projectId a pouzit dalej v kode

                val listSesLOLM: List<SesLOLM> = syncClass.listSesLOLM

                listSesLOLM.forEach { sesLOLM ->
                    val sessionSync: SessionSync = sesLOLM.ses
                    // ci session existuje
                    val session: Session? = async { sessionRepository.getSession(projectId, sessionSync.sessionStart) }.await()

                    // vo vsetkych pripadoch vybrat sessionId a pouzit dalej
                    var sessionId = 0L

                    if (session != null) {
                        // ak ano mozno Update
                        sessionId = session.sessionId
                    } else {
                        // ak nie tak Najst Najblizsiu
                        val nearSession: Session? = async { sessionRepository.getNearSession(projectId, sessionSync.sessionStart) }.await()

                        if (nearSession != null) {
                            // ak je tak mozno update
                            sessionId = nearSession.sessionId
                        } else {
                            // ak nie je tak Insert
                            // TODO pridat cislovanie sessionov v Projekte separatna funkcia
                            val newSession: Session = setNewSession(sessionSync)
                            sessionId = async { sessionRepository.insertSyncSession(newSession) }.await()
                        }
                    }

                    val listLOLM: List<LocOccLMouse> = sesLOLM.listLOLM

                    listLOLM.forEach { locOccLMouse ->
                        val localitySync: LocalitySync = locOccLMouse.loc
                        // ci existuje
                        val locality: Locality? = async { localityRepository.getLocalityByName(localitySync.localityName) }.await()

                        // zohnat localityId
                        var localityId = 0L

                        if (locality != null) {
                            // ak ano mozno Update
                            localityId = locality.localityId
                        } else {
                            // ak nie insert
                            val newLocality: Locality = setNewLocality(localitySync)
                            localityId = async { localityRepository.insertSyncLocality(newLocality) }.await()
                        }

                        val occasionSync: OccasionSync = locOccLMouse.occ
                        // ci existuje
                        val occasion: Occasion? = async { occasionRepository.getSyncOccasion(sessionId, occasionSync.occasionStart) }.await()

                        // zohant occasionId
                        var occasionId = 0L

                        if (occasion != null) {
                            // ak ano mozno Update
                            occasionId = occasion.occasionId
                        } else {
                            // ak nie Insert
                            val newOccasion: Occasion = setNewOccasion(occasionSync, sessionId, localityId)
                            occasionId = async { occasionRepository.insertOccasion(newOccasion) }.await()
                        }

                        val listMouse: List<MouseSync> = locOccLMouse.listMouse

                        listMouse.forEach { mouseSync ->
                            // ci existuje
                            val mouse: Mouse? = async { mouseRepository.getSyncMouse(occasionId, mouseSync.mouseCaught) }.await()

                            if (mouse != null) {
                                // ak ano mozno update
                            } else {
                                // ak nie Insert
                                val newMouse: Mouse = setNewMouse(mouseSync, localityId, occasionId)
                                launch { mouseRepository.inserSynctMouse(newMouse) }.join()
                            }
                        }
                    }
                }
            }
            dataDone.postValue(true)
        }
    }

    private val dataRetrieved = MutableLiveData<SyncData>()
    val dataRetrievedLive: LiveData<SyncData> by this::dataRetrieved

    fun retrieveData(date: Long) {
        viewModelScope.launch(Dispatchers.IO) {

            val mouseImages: Deferred<List<MouseImageSync>> = async { mouseImageRepository.getMouseImages(date) }

            val occasionImages: Deferred<List<OccasionImageSync>> = async { occasionImageRepository.getOccasionImages(date) }

            val specieImages: Deferred<List<SpecieImageSync>> = async { specieImageRepository.getSpecieImages(date) }

            val mouseList: List<Mouse> = async { mouseRepository.getMiceForSync(date) }.await()
            val occasionIds = mouseList.map { it.occasionID }

            val occasionList: List<Occasion> = async { occasionRepository.getOccasionForSync(occasionIds) }.await()
            val localityIds = occasionList.map { it.localityID }
            val sessionIds = occasionList.map { it.sessionID }

            val localityList: List<Locality> = async { localityRepository.getLocalityForSync(localityIds) }.await()

            val sessionList: List<Session> = async { sessionRepository.getSessionForSync(sessionIds) }.await()
            val projectIds = sessionList.map { it.projectID!! }

            val projectList: List<Project> = async { projectRepository.getProjectForSync(projectIds) }.await()

            val listSyncClass: List<SyncClass> = getSyncClassList(mouseList, occasionList, localityList, sessionList, projectList)

            val syncData = SyncData(
                listSyncClass,
                mouseImages.await(),
                occasionImages.await(),
                specieImages.await()
            )
            dataRetrieved.postValue(syncData)
        }
    }

    private fun getSyncClassList(mouseList: List<Mouse>, occasionList: List<Occasion>, localityList: List<Locality>, sessionList: List<Session>, projectList: List<Project>): List<SyncClass> {

        val sortedMice: Map<Long, List<MouseSync>> = mouseSyncList.groupBy { it.occasionID }

        val listLocOccMice: MutableList<LocOccLMouse> = mutableListOf()

        sortedMice.forEach { (occasionID, mouseList) ->
            val occasionSync: OccasionSync? = occasionSyncList.find { it.occasionId == occasionID }
            val localitySync: LocalitySync? = localitySyncList.find { it.localityId == occasionSync?.localityID!! }

            val locOccLMouse = LocOccLMouse(localitySync!!, occasionSync!!, mouseList)
            listLocOccMice.add(locOccLMouse)
        }

        val sortedLocOccMice: Map<OccasionSync, List<LocOccLMouse>> = listLocOccMice.groupBy { it.occ }

        val listSesLOLM: MutableList<SesLOLM> = mutableListOf()

        sortedLocOccMice.forEach { (occasion, list) ->
            val sessionSync: SessionSync? = sessionSyncList.find { it.sessionId ==  occasion.sessionID }

            val sesLOLM = SesLOLM(sessionSync!!, list)
            listSesLOLM.add(sesLOLM)
        }

        val sortedSesLOLM: Map<SessionSync, List<SesLOLM>> = listSesLOLM.groupBy { it.ses }

        val syncClassList: MutableList<SyncClass> = mutableListOf()

        sortedSesLOLM.forEach { (sessionSync, list) ->
            val projectSync: ProjectSync? = projectSyncList.find { it.projectId == sessionSync.projectID }

            val syncClass = SyncClass(projectSync!!, list)
            syncClassList.add(syncClass)
        }

        return syncClassList
    }

    private fun getImageSync(): ImageSync {
        val imageSync = ImageSync(listMouseImageSync, listOccasionImageSync, listSpecieImageSync)
        return imageSync
    }

    private fun getMouseFileList(): List<File> {
        val files: List<File> = listMouseImageSync.map { File(it.path) }
        return files
    }

    private fun getOccasionFileList(): List<File> {
        val fileList: List<File> = listOccasionImageSync.map { File(it.path) }
        return fileList
    }

    private fun getSpecieFileList(): List<File> {
        val fileList: List<File> = listSpecieImageSync.map { File(it.path) }
        return fileList
    }

    private fun setNewMouse(mouseSync: MouseSync, localityId: Long, occasionId: Long) = Mouse(
        0,
        mouseSync.mouseIid,
        mouseSync.code,
        mouseSync.deviceID,
        mouseSync.primeMouseID,
        mouseSync.speciesID,
        mouseSync.protocolID,
        occasionId,
        localityId,
        mouseSync.trapID,
        Calendar.getInstance().time,
        null,
        mouseSync.sex,
        mouseSync.age,
        mouseSync.gravidity,
        mouseSync.lactating,
        mouseSync.sexActive,
        mouseSync.weight,
        mouseSync.recapture,
        mouseSync.captureID,
        mouseSync.body,
        mouseSync.tail,
        mouseSync.feet,
        mouseSync.ear,
        mouseSync.testesLength,
        mouseSync.testesWidth,
        mouseSync.embryoRight,
        mouseSync.embryoLeft,
        mouseSync.embryoDiameter,
        mouseSync.MC,
        mouseSync.MCright,
        mouseSync.MCleft,
        mouseSync.note,
        mouseSync.mouseCaught
    )

    private fun setNewOccasion(occasionSync: OccasionSync, sessionId: Long, localityId: Long) = Occasion(
        0,
        occasionSync.occasion,
        localityId,
        sessionId,
        occasionSync.methodID,
        occasionSync.methodTypeID,
        occasionSync.trapTypeID,
        occasionSync.envTypeID,
        occasionSync.vegetTypeID,
        Calendar.getInstance().time,
        null,
        occasionSync.gotCaught,
        occasionSync.numTraps,
        occasionSync.numMice,
        occasionSync.temperature,
        occasionSync.weather,
        occasionSync.leg,
        occasionSync.note,
        occasionSync.occasionStart
    )

    private fun setNewLocality(localitySync: LocalitySync) = Locality(
        0,
        localitySync.localityName,
        Calendar.getInstance().time,
        null,
        localitySync.xA,
        localitySync.yA,
        localitySync.xB,
        localitySync.yB,
        localitySync.numSessions,
        localitySync.note
    )

    private fun setNewProject(projectSync: ProjectSync) = Project(
        0,
        projectSync.projectName,
        Calendar.getInstance().time,
        null,
        projectSync.numLocal,
        projectSync.numMice,
        projectSync.projectStart
    )

    private fun setNewSession(sessionSync: SessionSync) = Session(
        0,
        sessionSync.session,
        sessionSync.projectID,
        sessionSync.numOcc,
        Calendar.getInstance().time,
        null,
        sessionSync.sessionStart
    )

}