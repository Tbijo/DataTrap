package com.example.datatrap.mouse.domain.model

data class MouseOccList(
    val mouseId: String,
    val primeMouseID: Long?,
    val mouseCode: Int?,
    val specieCode: String,
    val mouseCaught: Long,
    val sex: String?,
    var deviceID: String
)
