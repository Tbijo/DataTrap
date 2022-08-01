package com.example.datatrap.models.tuples

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MouseOccList(
    val mouseId: Long,
    val primeMouseID: Long?,
    val mouseCode: Int?,
    val specieCode: String,
    val mouseCaught: Long,
    val sex: String?,
    var deviceID: String
): Parcelable
