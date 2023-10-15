package com.example.datatrap.sync.data.remote

data class MouseSync(
    val mouseIid: Long,
    val code: Int?,
    val deviceID: String,
    val primeMouseID: Long?,
    val speciesID: Long,
    val protocolID: Long?,
    val occasionID: Long,
    val localityID: Long,
    val trapID: Int?,
    val sex: String?,
    val age: String?,
    val gravidity: Boolean?,
    val lactating: Boolean?,
    val sexActive: Boolean?,
    val weight: Float?,
    val recapture: Boolean?,
    val captureID: String?,
    val body: Float?,
    val tail: Float?,
    val feet: Float?,
    val ear: Float?,
    val testesLength: Float?,
    val testesWidth: Float?,
    val embryoRight: Int?,
    val embryoLeft: Int?,
    val embryoDiameter: Float?,
    val MC: Boolean?,
    val MCright: Int?,
    val MCleft: Int?,
    val note: String?,
    val mouseCaught: Long
)
data class OccasionSync(
    val occasion: Int,
    val localityID: Long,
    val sessionID: Long,
    val methodID: Long,
    val methodTypeID: Long,
    val trapTypeID: Long,
    val envTypeID: Long?,
    val vegetTypeID: Long?,
    val gotCaught: Boolean?,
    val numTraps: Int,
    val numMice: Int?,
    val temperature: Float?,
    val weather: String?,
    val leg: String,
    val note: String?,
    val occasionStart: Long
)
data class LocalitySync(
    val localityName: String,
    val xA: Float?,
    val yA: Float?,
    val xB: Float?,
    val yB: Float?,
    val numSessions: Int,
    val note: String?
)
data class SessionSync(
    val session: Int,
    val projectID: Long,
    val numOcc: Int,
    val sessionStart: Long
)
data class ProjectSync(
    val projectName: String,
    val numLocal: Int,
    val numMice: Int,
    val projectStart: Long
)

data class LocOccLMouse(
    val loc: LocalitySync,
    val occ: OccasionSync,
    val listMouse: List<MouseSync>
)

data class SesLOLM(
    val ses: SessionSync,
    val listLOLM: List<LocOccLMouse>
)

data class SyncClass(
    val prj: ProjectSync,
    val listSesLOLM: List<SesLOLM>
)