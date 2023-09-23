package com.example.datatrap.mouse.domain.model

data class MouseLog(
    val mouseCaught: String,
    val localityName: String,
    val trapID: Int?,
    val weight: Float?,
    val sexActive: String?,
    val gravidity: String?,
    val lactating: String?,
)