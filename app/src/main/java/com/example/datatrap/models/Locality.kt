package com.example.datatrap.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity
data class Locality(

    @PrimaryKey(autoGenerate = true)
    var localityId: Long,

    var localityName: String,

    var deviceID: String,

    var localityDateTimeCreated: Date,

    var localityDateTimeUpdated: Date?,

    var xA: Float, // latitudeA

    var yA: Float, // longitudeA

    var xB: Float, // latitudeB

    var yB: Float, // longitudeB

    var numSessions: Int,

    var note: String?
): Parcelable
