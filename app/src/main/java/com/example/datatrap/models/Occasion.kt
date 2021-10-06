package com.example.datatrap.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "occasions", foreignKeys = [
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

    @ColumnInfo(name = "Occasion")
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

    @ColumnInfo(name = "Got_caught")
    var gotCaught: Int?, // boolean

    @ColumnInfo(name = "No_traps")
    var numTraps: Int?,

    @ColumnInfo(name = "Num_mice")
    var numMice: Int?,

    @ColumnInfo(name = "Temperature")
    var temperature: Float?,

    @ColumnInfo(name = "Weather")
    var weather: String?,

    @ColumnInfo(name = "Leg")
    var leg: String,

    @ColumnInfo(name = "Note_occ")
    var note: String?,

    // cudzi kluc
    @ColumnInfo(name = "Img_occ")
    var imgName: String?
): Parcelable
