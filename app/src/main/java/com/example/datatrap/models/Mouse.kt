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
@Entity(tableName = "sm", foreignKeys = [
    ForeignKey(entity = Specie::class, parentColumns = ["specieId"], childColumns = ["speciesID"], onDelete = CASCADE),
    ForeignKey(entity = Protocol::class, parentColumns = ["protocolId"], childColumns = ["protocolID"], onDelete = CASCADE),
    ForeignKey(entity = Occasion::class, parentColumns = ["occasionId"], childColumns = ["occasionID"], onDelete = CASCADE),
    ForeignKey(entity = Locality::class, parentColumns = ["localityId"], childColumns = ["localityID"], onDelete = CASCADE)
])
data class Mouse(

    @PrimaryKey(autoGenerate = true)
    var mouseId: Long,

    @ColumnInfo(name = "Code")
    var code: Int?, //pazure

    var deviceID: String,

    var primeMouseID: Long?,

    // cudzi kluc
    var speciesID: Long,

    // cudzi kluc
    var protocolID: Long?,

    // cudzi kluc
    var occasionID: Long,

    // cudzi kluc
    var localityID: Long,

    @ColumnInfo(name = "Trap_ID")
    var trapID: String,

    var mouseDateTimeCreated: Date,

    var mouseDateTimeUpdated: Date?,

    @ColumnInfo(name = "Sex")
    var sex: String?, //list male female null

    @ColumnInfo(name = "Age")
    var age: String?, //vytvor list hodnot

    @ColumnInfo(name = "Gravidity")
    var gravidity: Int?, //bool

    @ColumnInfo(name = "Lactating")
    var lactating: Int?, //bool

    @ColumnInfo(name = "Sex_active")
    var sexActive: Int?, //bool

    @ColumnInfo(name = "Weight")
    var weight: Float?,

    @ColumnInfo(name = "Recapture")
    var recapture: Int?, //bool

    @ColumnInfo(name = "Capture_ID")
    var captureID: String?, //vytvor list hodnot

    @ColumnInfo(name = "Body")
    var body: Float?,

    @ColumnInfo(name = "Tail")
    var tail: Float?,

    @ColumnInfo(name = "Feet")
    var feet: Float?,

    @ColumnInfo(name = "Ear")
    var ear: Float?,

    @ColumnInfo(name = "Testes_length")
    var testesLength: Float?,

    @ColumnInfo(name = "Testes_width")
    var testesWidth: Float?,

    @ColumnInfo(name = "Embryo_right")
    var embryoRight: Int?,

    @ColumnInfo(name = "Embryo_left")
    var embryoLeft: Int?,

    @ColumnInfo(name = "Embryo_diameter")
    var embryoDiameter: Float?,

    var MC: Int?, //bool

    @ColumnInfo(name = "MC_right")
    var MCright: Int?,

    @ColumnInfo(name = "MC_left")
    var MCleft: Int?,

    @ColumnInfo(name = "Note_sm")
    var note: String?,

    @ColumnInfo(name = "Img_sm") // cudzi kluc
    var imgName: String?,
): Parcelable
