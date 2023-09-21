package com.example.datatrap.mouse.domain.model

data class MouseView(
    var mouseId: String,
    val body: Float?,
    val tail: Float?,
    val feet: Float?,
    val ear: Float?,
    val mouseCaught: String,
    val gravidity: Boolean?,
    val lactating: Boolean?,
    val sexActive: Boolean?,
    val age: String?,
    val sex: String?,
    val weight: Float?,
    val note: String?,
    val testesLength: Float?,
    val testesWidth: Float?,
    val embryoRight: Int?,
    val embryoLeft: Int?,
    val embryoDiameter: Float?,
    val mc: Boolean?,
    val mcRight: Int?,
    val mcLeft: Int?,
    val specieFullName: String?, // Specie
    val specieCode: String,
    val legit: String, // Occasion
    val projectName: String, // Project
)