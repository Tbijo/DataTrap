package com.example.datatrap.mouse.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.example.datatrap.locality.data.locality.LocalityEntity
import com.example.datatrap.occasion.data.occasion.OccasionEntity
import com.example.datatrap.settings.protocol.data.ProtocolEntity
import com.example.datatrap.specie.data.SpecieEntity
import java.time.ZonedDateTime
import java.util.UUID

@Entity(foreignKeys = [
    ForeignKey(entity = SpecieEntity::class, parentColumns = ["specieId"], childColumns = ["speciesID"], onDelete = CASCADE),
    ForeignKey(entity = ProtocolEntity::class, parentColumns = ["protocolId"], childColumns = ["protocolID"], onDelete = CASCADE),
    ForeignKey(entity = OccasionEntity::class, parentColumns = ["occasionId"], childColumns = ["occasionID"], onDelete = CASCADE),
    ForeignKey(entity = LocalityEntity::class, parentColumns = ["localityId"], childColumns = ["localityID"], onDelete = CASCADE)
])
data class MouseEntity(

    @PrimaryKey
    var mouseId: String = UUID.randomUUID().toString(),

    var code: Int?, //claws

    var primeMouseID: String?,

    // FE
    @ColumnInfo(index = true)
    var speciesID: String,

    @ColumnInfo(index = true)
    var protocolID: String?,

    @ColumnInfo(index = true)
    var occasionID: String,

    @ColumnInfo(index = true)
    var localityID: String,

    var trapID: Int?,

    var mouseDateTimeCreated: ZonedDateTime = ZonedDateTime.now(),

    var mouseDateTimeUpdated: ZonedDateTime?,

    var sex: String?, //enum

    var age: String?, //enum

    var gravidity: Boolean?,

    var lactating: Boolean?,

    var sexActive: Boolean?,

    var weight: Float?,

    var recapture: Boolean?,

    var captureID: String?, //enum

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

    var mouseCaught: ZonedDateTime = ZonedDateTime.now(),
)