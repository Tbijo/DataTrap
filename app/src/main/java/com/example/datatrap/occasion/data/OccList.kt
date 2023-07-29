package com.example.datatrap.occasion.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class OccList(
    val occasionId: Long,
    val localityID: Long,
    val occasion: Int,
    val occasionStart: Long,
    val numMice: Int?,
    val numTraps: Int
): Parcelable
