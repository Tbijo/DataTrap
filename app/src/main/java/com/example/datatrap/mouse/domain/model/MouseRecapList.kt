package com.example.datatrap.mouse.domain.model

data class MouseRecapList(
    val mouseId: String,
    val primeMouseID: String?,
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
)