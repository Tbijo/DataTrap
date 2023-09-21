package com.example.datatrap.sync.data.remote

data class MouseSync(
    var mouseIid: Long,
    var code: Int?,
    var deviceID: String,
    var primeMouseID: Long?,
    var speciesID: Long,
    var protocolID: Long?,
    var occasionID: Long,
    var localityID: Long,
    var trapID: Int?,
    var sex: String?,
    var age: String?,
    var gravidity: Boolean?,
    var lactating: Boolean?,
    var sexActive: Boolean?,
    var weight: Float?,
    var recapture: Boolean?,
    var captureID: String?,
    var body: Float?,
    var tail: Float?,
    var feet: Float?,
    var ear: Float?,
    var testesLength: Float?,
    var testesWidth: Float?,
    var embryoRight: Int?,
    var embryoLeft: Int?,
    var embryoDiameter: Float?,
    var MC: Boolean?,
    var MCright: Int?,
    var MCleft: Int?,
    var note: String?,
    var mouseCaught: Long
)
data class OccasionSync(
    var occasion: Int,
    var localityID: Long,
    var sessionID: Long,
    var methodID: Long,
    var methodTypeID: Long,
    var trapTypeID: Long,
    var envTypeID: Long?,
    var vegetTypeID: Long?,
    var gotCaught: Boolean?,
    var numTraps: Int,
    var numMice: Int?,
    var temperature: Float?,
    var weather: String?,
    var leg: String,
    var note: String?,
    var occasionStart: Long
)
data class LocalitySync(
    var localityName: String,
    var xA: Float?,
    var yA: Float?,
    var xB: Float?,
    var yB: Float?,
    var numSessions: Int,
    var note: String?
)
data class SessionSync(
    var session: Int,
    var projectID: Long,
    var numOcc: Int,
    var sessionStart: Long
)
data class ProjectSync(
    var projectName: String,
    var numLocal: Int,
    var numMice: Int,
    var projectStart: Long
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