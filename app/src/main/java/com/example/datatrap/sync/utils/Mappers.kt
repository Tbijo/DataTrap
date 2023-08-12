package com.example.datatrap.sync.utils

import com.example.datatrap.models.*
import com.example.datatrap.models.sync.*
import java.util.*

object Mappers {
    fun toSyncClassList(mouseList: List<Mouse>, occasionList: List<Occasion>,
                                localityList: List<Locality>, sessionList: List<Session>,
                                projectList: List<Project>): List<SyncClass> {

        val sortedMice: Map<Long, List<Mouse>> = mouseList.groupBy { it.occasionID }

        val listLocOccMice: MutableList<LocOccLMouse> = mutableListOf()

        sortedMice.forEach { (occasionID, mouseList) ->
            val occasion: Occasion? = occasionList.find { it.occasionId == occasionID }
            val occasionSync: OccasionSync = toOccasionSync(occasion!!)
            val locality: Locality? = localityList.find { it.localityId == occasion.localityID }
            val localitySync: LocalitySync = toLocalitySync(locality!!)

            val mouseSyncList: List<MouseSync> = mouseList.map { toMouseSync(it) }

            val locOccLMouse = LocOccLMouse(localitySync, occasionSync, mouseSyncList)
            listLocOccMice.add(locOccLMouse)
        }

        val sortedLocOccMice: Map<OccasionSync, List<LocOccLMouse>> =
            listLocOccMice.groupBy { it.occ }

        val listSesLOLM: MutableList<SesLOLM> = mutableListOf()

        sortedLocOccMice.forEach { (occasion, list) ->
            val session: Session? = sessionList.find { it.sessionId == occasion.sessionID }
            val sessionSync: SessionSync = toSessionSync(session!!)

            val sesLOLM = SesLOLM(sessionSync, list)
            listSesLOLM.add(sesLOLM)
        }

        val sortedSesLOLM: Map<SessionSync, List<SesLOLM>> = listSesLOLM.groupBy { it.ses }

        val syncClassList: MutableList<SyncClass> = mutableListOf()

        sortedSesLOLM.forEach { (sessionSync, list) ->
            val project: Project? = projectList.find { it.projectId == sessionSync.projectID }
            val projectSync: ProjectSync = toProjectSync(project!!)

            val syncClass = SyncClass(projectSync, list)
            syncClassList.add(syncClass)
        }

        return syncClassList
    }

    fun toMouseSync(mouse: Mouse): MouseSync = MouseSync(
        mouse.mouseIid,
        mouse.code,
        mouse.deviceID,
        mouse.primeMouseID,
        mouse.speciesID,
        mouse.protocolID,
        mouse.occasionID,
        mouse.localityID,
        mouse.trapID,
        mouse.sex,
        mouse.age,
        mouse.gravidity,
        mouse.lactating,
        mouse.sexActive,
        mouse.weight,
        mouse.recapture,
        mouse.captureID,
        mouse.body,
        mouse.tail,
        mouse.feet,
        mouse.ear,
        mouse.testesLength,
        mouse.testesWidth,
        mouse.embryoRight,
        mouse.embryoLeft,
        mouse.embryoDiameter,
        mouse.MC,
        mouse.MCright,
        mouse.MCleft,
        mouse.note,
        mouse.mouseCaught
    )

    fun toOccasionSync(occasion: Occasion): OccasionSync = OccasionSync(
        occasion.occasion,
        occasion.localityID,
        occasion.sessionID,
        occasion.methodID,
        occasion.methodTypeID,
        occasion.trapTypeID,
        occasion.envTypeID,
        occasion.vegetTypeID,
        occasion.gotCaught,
        occasion.numTraps,
        occasion.numMice,
        occasion.temperature,
        occasion.weather,
        occasion.leg,
        occasion.note,
        occasion.occasionStart
    )

    fun toLocalitySync(locality: Locality): LocalitySync = LocalitySync(
        locality.localityName,
        locality.xA,
        locality.yA,
        locality.xB,
        locality.yB,
        locality.numSessions,
        locality.note
    )

    fun toSessionSync(session: Session): SessionSync = SessionSync(
        session.session,
        session.projectID!!,
        session.numOcc,
        session.sessionStart
    )

    fun toProjectSync(project: Project): ProjectSync = ProjectSync(
        project.projectName,
        project.numLocal,
        project.numMice,
        project.projectStart
    )

    fun setNewMouse(mouseSync: MouseSync, localityId: Long, occasionId: Long) = Mouse(
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

    fun setNewOccasion(occasionSync: OccasionSync, sessionId: Long, localityId: Long) = Occasion(
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

    fun setNewLocality(localitySync: LocalitySync) = Locality(
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

    fun setNewProject(projectSync: ProjectSync) = Project(
        0,
        projectSync.projectName,
        Calendar.getInstance().time,
        null,
        projectSync.numLocal,
        projectSync.numMice,
        projectSync.projectStart
    )

    fun setNewSession(sessionSync: SessionSync) = Session(
        0,
        sessionSync.session,
        sessionSync.projectID,
        sessionSync.numOcc,
        Calendar.getInstance().time,
        null,
        sessionSync.sessionStart
    )
}