package com.example.datatrap.specie.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity
data class Specie(

    @PrimaryKey(autoGenerate = true)
    var specieId: Long,

    var speciesCode: String,

    var fullName: String,

    var synonym: String?,

    var authority: String?,

    var description: String?,

    var isSmallMammal: Boolean, //boolean

    var upperFingers: Int?,

    var minWeight: Float?, //kontrloly pri vstupoch

    var maxWeight: Float?, //kontrloly pri vstupoch

    var bodyLength: Float?,

    var tailLength: Float?,

    var feetLengthMin: Float?,

    var feetLengthMax: Float?,

    var note: String?,

    var specieDateTimeCreated: Date,

    var specieDateTimeUpdated: Date?
): Parcelable
