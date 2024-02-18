package com.example.datatrap.mouse.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.example.datatrap.locality.data.locality.LocalityEntity
import com.example.datatrap.occasion.data.occasion.OccasionEntity
import com.example.datatrap.settings.data.protocol.ProtocolEntity
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
    val mouseId: String = UUID.randomUUID().toString(),

    val code: Int?, //claws

    val primeMouseID: String?,

    @ColumnInfo(index = true)
    val speciesID: String,

    @ColumnInfo(index = true)
    val protocolID: String?,

    @ColumnInfo(index = true)
    val occasionID: String,

    @ColumnInfo(index = true)
    val localityID: String,

    val trapID: Int?,

    val mouseDateTimeCreated: ZonedDateTime = ZonedDateTime.now(),

    val mouseDateTimeUpdated: ZonedDateTime?,

    val sex: String?, //enum

    val age: String?, //enum

    val gravidity: Boolean?,

    val lactating: Boolean?,

    val sexActive: Boolean?,

    val weight: Float?,

    val recapture: Boolean?,

    val captureID: String?, //enum

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

    val mouseCaught: ZonedDateTime = ZonedDateTime.now(),
)