package com.example.datatrap.locality.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class LocList(

    var localityId: Long,

    var localityName: String,

    var dateTime: Date,

    var xA: Float?,

    var yA: Float?,

    var numSessions: Int
): Parcelable
