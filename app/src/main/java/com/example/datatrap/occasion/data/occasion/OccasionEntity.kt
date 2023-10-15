package com.example.datatrap.occasion.data.occasion

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.example.datatrap.locality.data.locality.LocalityEntity
import com.example.datatrap.session.data.SessionEntity
import com.example.datatrap.settings.envtype.data.EnvTypeEntity
import com.example.datatrap.settings.method.data.MethodEntity
import com.example.datatrap.settings.methodtype.data.MethodTypeEntity
import com.example.datatrap.settings.traptype.data.TrapTypeEntity
import com.example.datatrap.settings.vegettype.data.VegetTypeEntity
import java.time.ZonedDateTime
import java.util.UUID

@Entity(foreignKeys = [
    ForeignKey(entity = LocalityEntity::class, parentColumns = ["localityId"], childColumns = ["localityID"], onDelete = CASCADE),
    ForeignKey(entity = SessionEntity::class, parentColumns = ["sessionId"], childColumns = ["sessionID"], onDelete = CASCADE),
    ForeignKey(entity = MethodEntity::class, parentColumns = ["methodId"], childColumns = ["methodID"], onDelete = CASCADE),
    ForeignKey(entity = MethodTypeEntity::class, parentColumns = ["methodTypeId"], childColumns = ["methodTypeID"], onDelete = CASCADE),
    ForeignKey(entity = TrapTypeEntity::class, parentColumns = ["trapTypeId"], childColumns = ["trapTypeID"], onDelete = CASCADE),
    ForeignKey(entity = EnvTypeEntity::class, parentColumns = ["envTypeId"], childColumns = ["envTypeID"], onDelete = CASCADE),
    ForeignKey(entity = VegetTypeEntity::class, parentColumns = ["vegetTypeId"], childColumns = ["vegetTypeID"], onDelete = CASCADE)
])
data class OccasionEntity(

    @PrimaryKey
    val occasionId: String = UUID.randomUUID().toString(),

    val occasion: Int,

    // cudzi kluc
    @ColumnInfo(index = true)
    val localityID: String,

    // cudzi kluc
    @ColumnInfo(index = true)
    val sessionID: String,

    // cudzi kluc
    @ColumnInfo(index = true)
    val methodID: String,

    // cudzi kluc
    @ColumnInfo(index = true)
    val methodTypeID: String,

    // cudzi kluc
    @ColumnInfo(index = true)
    val trapTypeID: String,

    // cudzi kluc
    @ColumnInfo(index = true)
    val envTypeID: String?,

    // cudzi kluc
    @ColumnInfo(index = true)
    val vegetTypeID: String?,

    val occasionDateTimeCreated: ZonedDateTime = ZonedDateTime.now(),

    val occasionDateTimeUpdated: ZonedDateTime?,

    val gotCaught: Boolean?,

    val numTraps: Int,

    val numMice: Int?,

    val temperature: Float?,

    val weather: String?,

    val leg: String,

    val note: String?,

    val occasionStart: ZonedDateTime = ZonedDateTime.now(),
)