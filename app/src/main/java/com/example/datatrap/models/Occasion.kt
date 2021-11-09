package com.example.datatrap.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(foreignKeys = [
    ForeignKey(entity = Locality::class, parentColumns = ["localityId"], childColumns = ["localityID"], onDelete = CASCADE),
    ForeignKey(entity = Session::class, parentColumns = ["sessionId"], childColumns = ["sessionID"], onDelete = CASCADE),
    ForeignKey(entity = Method::class, parentColumns = ["methodId"], childColumns = ["methodID"], onDelete = CASCADE),
    ForeignKey(entity = MethodType::class, parentColumns = ["methodTypeId"], childColumns = ["methodTypeID"], onDelete = CASCADE),
    ForeignKey(entity = TrapType::class, parentColumns = ["trapTypeId"], childColumns = ["trapTypeID"], onDelete = CASCADE),
    ForeignKey(entity = EnvType::class, parentColumns = ["envTypeId"], childColumns = ["envTypeID"], onDelete = CASCADE),
    ForeignKey(entity = VegetType::class, parentColumns = ["vegetTypeId"], childColumns = ["vegetTypeID"], onDelete = CASCADE)
])
data class Occasion(

    @PrimaryKey(autoGenerate = true)
    var occasionId: Long,

    var occasion: Int,

    var deviceID: String,

    // cudzi kluc
    var localityID: Long,

    // cudzi kluc
    var sessionID: Long,

    // cudzi kluc
    var methodID: Long,

    // cudzi kluc
    var methodTypeID: Long,

    // cudzi kluc
    var trapTypeID: Long,

    // cudzi kluc
    var envTypeID: Long?,

    // cudzi kluc
    var vegetTypeID: Long?,

    var occasionDateTimeCreated: Date,

    var occasionDateTimeUpdated: Date?,

    var gotCaught: Boolean?, // boolean

    var numTraps: Int,

    var numMice: Int?,

    var temperature: Float?,

    var weather: String?,

    var leg: String,

    var note: String?,

    // cudzi kluc
    var imgName: String?
): Parcelable
