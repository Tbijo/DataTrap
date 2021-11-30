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
@Entity(foreignKeys = [
    ForeignKey(entity = Specie::class, parentColumns = ["specieId"], childColumns = ["speciesID"], onDelete = CASCADE),
    ForeignKey(entity = Protocol::class, parentColumns = ["protocolId"], childColumns = ["protocolID"], onDelete = CASCADE),
    ForeignKey(entity = Occasion::class, parentColumns = ["occasionId"], childColumns = ["occasionID"], onDelete = CASCADE),
    ForeignKey(entity = Locality::class, parentColumns = ["localityId"], childColumns = ["localityID"], onDelete = CASCADE)
])
data class Mouse(

    @PrimaryKey(autoGenerate = true)
    var mouseId: Long,

    var code: Int?, //pazure

    var deviceID: String,

    var primeMouseID: Long?,

    // cudzi kluc
    @ColumnInfo(index = true)
    var speciesID: Long,

    // cudzi kluc
    @ColumnInfo(index = true)
    var protocolID: Long?,

    // cudzi kluc
    @ColumnInfo(index = true)
    var occasionID: Long,

    // cudzi kluc
    @ColumnInfo(index = true)
    var localityID: Long,

    var trapID: Int?,

    var mouseDateTimeCreated: Date,

    var mouseDateTimeUpdated: Date?,

    var sex: String?, //list male female null

    var age: String?, //vytvor list hodnot

    var gravidity: Boolean?, //bool

    var lactating: Boolean?, //bool

    var sexActive: Boolean?, //bool

    var weight: Float?,

    var recapture: Boolean?, //bool

    var captureID: String?, //vytvor list hodnot

    var body: Float?,

    var tail: Float?,

    var feet: Float?,

    var ear: Float?,

    var testesLength: Float?,

    var testesWidth: Float?,

    var embryoRight: Int?,

    var embryoLeft: Int?,

    var embryoDiameter: Float?,

    var MC: Boolean?, //bool

    var MCright: Int?,

    var MCleft: Int?,

    var note: String?

): Parcelable
