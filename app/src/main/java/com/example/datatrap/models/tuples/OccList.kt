package com.example.datatrap.models.tuples

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class OccList(
    val occasionId: Long,
    val localityID: Long,
    val occasion: Int,
    val dateTime: Date,
    val numMice: Int?,
    val numTraps: Int,
    val imgName: String?
): Parcelable
