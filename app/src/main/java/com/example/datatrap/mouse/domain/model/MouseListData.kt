package com.example.datatrap.mouse.domain.model

data class MouseListData(
    val mouseId: String,
    val primeMouseID: String?,
    val mouseCode: Int?,
    val specieCode: String,
    val mouseCaught: String,
    val sex: String?,
)