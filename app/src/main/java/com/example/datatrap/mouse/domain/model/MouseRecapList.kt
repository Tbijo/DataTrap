package com.example.datatrap.mouse.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MouseRecapList(
    val mouseId: Long,
    val primeMouseID: Long?,
    val code: Int?,
    val age: String?,
    val weight: Float?,
    val sex: String?,
    val gravidity: Boolean?,
    val lactating: Boolean?,
    val sexActive: Boolean,
    val localityName: String, // Locality
    val specieCode: String, // Specie
    val mouseCaught: Long
): Parcelable