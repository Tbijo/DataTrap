package com.example.datatrap.models.tuples

data class MouseLog(
    val mouseCaught: Long,
    val localityName: String,
    val trapID: Int?,
    val weight: Float?,
    val sexActive: Boolean,
    val gravidity: Boolean?,
    val lactating: Boolean?
)
