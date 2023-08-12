package com.example.datatrap.sync.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.camera.data.mouse_image.MouseImageRepository
import com.example.datatrap.camera.data.occasion_image.OccasionImageRepository
import com.example.datatrap.core.data.pref.PrefViewModel
import com.example.datatrap.locality.data.LocalityEntity
import com.example.datatrap.locality.data.LocalityRepository
import com.example.datatrap.mouse.data.MouseEntity
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.occasion.data.OccasionEntity
import com.example.datatrap.occasion.data.OccasionRepository
import com.example.datatrap.project.data.ProjectEntity
import com.example.datatrap.project.data.ProjectRepository
import com.example.datatrap.session.data.SessionEntity
import com.example.datatrap.session.data.SessionRepository
import com.example.datatrap.specie.data.specie_image.SpecieImageRepository
import com.example.datatrap.sync.data.DataTrapRepository
import com.example.datatrap.sync.data.ImageSync
import com.example.datatrap.sync.data.LocOccLMouse
import com.example.datatrap.sync.data.LocalitySync
import com.example.datatrap.sync.data.MouseImageSync
import com.example.datatrap.sync.data.MouseSync
import com.example.datatrap.sync.data.OccasionImageSync
import com.example.datatrap.sync.data.OccasionSync
import com.example.datatrap.sync.data.ProjectSync
import com.example.datatrap.sync.data.SesLOLM
import com.example.datatrap.sync.data.SessionSync
import com.example.datatrap.sync.data.SpecieImageSync
import com.example.datatrap.sync.data.SyncClass
import com.example.datatrap.sync.utils.Mappers.setNewLocality
import com.example.datatrap.sync.utils.Mappers.setNewMouse
import com.example.datatrap.sync.utils.Mappers.setNewOccasion
import com.example.datatrap.sync.utils.Mappers.setNewProject
import com.example.datatrap.sync.utils.Mappers.setNewSession
import com.example.datatrap.sync.utils.Mappers.toSyncClassList
import com.example.datatrap.sync.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
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
    private val specieImageRepository: SpecieImageRepository,
    private val dataTrapRepo: DataTrapRepository
) : ViewModel() {

    private val currentOpperation = MutableLiveData<String>()
    val currentOpperationLive: LiveData<String> by this::currentOpperation

    private val downloadError = MutableLiveData<String>()
    val downloadErrorLive: LiveData<String> by this::downloadError

    private val doneSyncCall = MutableLiveData<Boolean>()
    val doneSyncCallLive: LiveData<Boolean> by this::doneSyncCall

    private val prefViewModel: PrefViewModel by viewModels()
    private var lastSyncDate: Long? = null

    init {
        startSync("Loading Data")

        prefViewModel.readLastSyncDatePref.observe(viewLifecycleOwner) { date ->
            lastSyncDate = date
        }
    }

    private fun startSync(work: String) {
        // start
        binding.tvLoadMessage.text = work
        binding.tvLoadMessage.isVisible = true
        binding.loadingBar.isVisible = true
    }

    private fun endSync() {
        // end
        binding.tvLoadMessage.isVisible = false
        binding.loadingBar.isVisible = false
    }

    fun retrieveData(date: Long) {
        viewModelScope.launch(Dispatchers.IO) {

            // NACITAT DATA FOTIEK
            val mouseImagesSync: List<MouseImageSync> = mouseImageRepository.getMouseImages(date)

            val occasionImagesSync: List<OccasionImageSync> = occasionImageRepository.getOccasionImages(date)

            val specieImagesSync: List<SpecieImageSync> = specieImageRepository.getSpecieImages(date)

            val imageSync = ImageSync(mouseImagesSync, occasionImagesSync, specieImagesSync)

            currentOpperation.postValue("Sending Image Data")

            // ODOSLANIE FOTIEK
            if (imageSync.mouseImages.isNotEmpty() && imageSync.occasionImages.isNotEmpty() && imageSync.specieImages.isNotEmpty()) {
                launch { dataTrapRepo.uploadImageInfo(imageSync) }
            }
            if (imageSync.mouseImages.isNotEmpty()) {
                launch { dataTrapRepo.uploadMouseFiles(imageSync.mouseImages.map { File(it.path) }) }
            }
            if (imageSync.occasionImages.isNotEmpty()) {
                launch { dataTrapRepo.uploadOccasionFiles(imageSync.occasionImages.map { File(it.path) }) }
            }
            if (imageSync.specieImages.isNotEmpty()) {
                launch { dataTrapRepo.uploadSpecieFiles(imageSync.specieImages.map { File(it.path) }) }
            }

            currentOpperation.postValue("Loading Gathered Data")

            // NACITAT DATA O CHYTENYCH JEDINCOCH
            val mouseEntityList: List<MouseEntity> = mouseRepository.getMiceForSync(date)
            val occasionIds = mouseEntityList.map { it.occasionID }

            val occasionEntityList: List<OccasionEntity> = occasionRepository.getOccasionForSync(occasionIds)
            val localityIds = occasionEntityList.map { it.localityID }
            val sessionIds = occasionEntityList.map { it.sessionID }

            val localityEntityList: List<LocalityEntity> = localityRepository.getLocalityForSync(localityIds)

            val sessionEntityList: List<SessionEntity> = sessionRepository.getSessionForSync(sessionIds)
            val projectIds = sessionEntityList.map { it.projectID!! }

            val projectEntityList: List<ProjectEntity> = projectRepository.getProjectForSync(projectIds)

            val listSyncClass: List<SyncClass> = toSyncClassList(mouseEntityList, occasionEntityList, localityEntityList, sessionEntityList, projectEntityList)

            currentOpperation.postValue("Sending Gathered Data")

            dataTrapRepo.uploadData(listSyncClass)

            currentOpperation.postValue("Downloading Data")

            val rawData = dataTrapRepo.downloadData(date)

            currentOpperation.postValue("Saving Data")

            when (rawData) {
                is ResultWrapper.MySuccess -> {
                    if (rawData.value.isSuccessful) {
                        saveData(rawData.value.body()!!)
                    } else {
                        downloadError.postValue("No Response")
                    }
                }
                is ResultWrapper.MyError -> {
                    downloadError.postValue("\"${rawData.errorM} ${rawData.code}\"")
                }
            }

        }
    }

    private suspend fun saveData(listSyncClass: List<SyncClass>) {
            listSyncClass.forEach { syncClass ->
                val projectSync: ProjectSync = syncClass.prj
                // ci projekt je v Databaze
                val projectEntity: ProjectEntity? = projectRepository.getProjectByName(projectSync.projectName)

                val projectId: Long = if (projectEntity != null) {
                    // ak je mozno Update
                    // TODO Aplikacia prinesie vzdy vacsie cislo
                    // nechat tuto starost na nich pred pouzitim vzdy syncronizovat
                    // TODO ak bude mensie cislo tak nemenit hodnotu v stlpci
                    // lebo aj tak kazde pridanie bude hodnotu samo zvacsovat
                    projectEntity.projectId
                } else {
                    // ak nie je tak Insert
                    val newProjectEntity: ProjectEntity = setNewProject(projectSync)
                    projectRepository.insertSyncProject(newProjectEntity)
                }
                // v oboch pripadoch vybrat projectId a pouzit dalej v kode

                val listSesLOLM: List<SesLOLM> = syncClass.listSesLOLM

                listSesLOLM.forEach { sesLOLM ->
                    val sessionSync: SessionSync = sesLOLM.ses
                    // ci session existuje
                    val sessionEntity: SessionEntity? = sessionRepository.getSession(projectId, sessionSync.sessionStart)

                    // vo vsetkych pripadoch vybrat sessionId a pouzit dalej
                    val sessionId: Long

                    if (sessionEntity != null) {
                        // ak ano mozno Update
                        sessionId = sessionEntity.sessionId
                    } else {
                        // ak nie tak Najst Najblizsiu
                        val nearSessionEntity: SessionEntity? = sessionRepository.getNearSession(projectId, sessionSync.sessionStart)

                        sessionId = if (nearSessionEntity != null) {
                            // ak je tak mozno update
                            nearSessionEntity.sessionId
                        } else {
                            // ak nie je tak Insert
                            // TODO pridat cislovanie sessionov v Projekte separatna funkcia
                            val newSessionEntity: SessionEntity = setNewSession(sessionSync)
                            sessionRepository.insertSyncSession(newSessionEntity)
                        }
                    }

                    val listLOLM: List<LocOccLMouse> = sesLOLM.listLOLM

                    listLOLM.forEach { locOccLMouse ->
                        val localitySync: LocalitySync = locOccLMouse.loc
                        // ci existuje
                        val localityEntity: LocalityEntity? = localityRepository.getLocalityByName(localitySync.localityName)

                        // zohnat localityId

                        val localityId: Long = if (localityEntity != null) {
                            // ak ano mozno Update
                            localityEntity.localityId
                        } else {
                            // ak nie insert
                            val newLocalityEntity: LocalityEntity = setNewLocality(localitySync)
                            localityRepository.insertSyncLocality(newLocalityEntity)
                        }

                        val occasionSync: OccasionSync = locOccLMouse.occ
                        // ci existuje
                        val occasionEntity: OccasionEntity? = occasionRepository.getSyncOccasion(sessionId, occasionSync.occasionStart)

                        // zohant occasionId

                        val occasionId: Long = if (occasionEntity != null) {
                            // ak ano mozno Update
                            occasionEntity.occasionId
                        } else {
                            // ak nie Insert
                            val newOccasionEntity: OccasionEntity = setNewOccasion(occasionSync, sessionId, localityId)
                            occasionRepository.insertOccasion(newOccasionEntity)
                        }

                        val listMouse: List<MouseSync> = locOccLMouse.listMouse

                        listMouse.forEach { mouseSync ->
                            // ci existuje
                            val mouseEntity: MouseEntity? = mouseRepository.getSyncMouse(occasionId, mouseSync.mouseCaught)

                            if (mouseEntity != null) {
                                // ak ano mozno update
                            } else {
                                // ak nie Insert
                                val newMouseEntity: MouseEntity = setNewMouse(mouseSync, localityId, occasionId)
                                mouseRepository.inserSynctMouse(newMouseEntity)
                            }
                        }
                    }
                }
            }
        doneSyncCall.postValue(true)
    }

}