package com.example.datatrap.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

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
    val occasionId: Long,

    @ColumnInfo(name = "Occasion")
    val occasion: Int,

    // cudzi kluc
    val localityID: Long,

    // cudzi kluc
    val sessionID: Long,

    // cudzi kluc
    val methodID: Long,

    // cudzi kluc
    val methodTypeID: Long,

    // cudzi kluc
    val trapTypeID: Long,

    // cudzi kluc
    val envTypeID: Long?,

    // cudzi kluc
    val vegetTypeID: Long?,

    @ColumnInfo(name = "Date")
    val date: String,

    @ColumnInfo(name = "Time")
    val time: String,

    @ColumnInfo(name = "Got_caught")
    val gotCaught: Int?, // boolean

    @ColumnInfo(name = "No_traps")
    val numTraps: Int?,

    @ColumnInfo(name = "Num_mice")
    val numMice: Int?,

    @ColumnInfo(name = "Temperature")
    val temperature: Float?,

    @ColumnInfo(name = "Weather")
    val weather: String?,

    @ColumnInfo(name = "Leg")
    val leg: String,

    @ColumnInfo(name = "Note_occ")
    val note: String?,

    // cudzi kluc
    @ColumnInfo(name = "Img_occ")
    val imgName: String?
): Parcelable
