package com.example.datatrap.models.tuples

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class MouseOccList(
    val mouseId: Long,
    val primeMouseID: Long?,
    val mouseCode: Int?,
    val specieCode: String,
    val dateTime: Date,
    val sex: String?
): Parcelable
