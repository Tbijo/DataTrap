package com.example.datatrap.models.tuples

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
