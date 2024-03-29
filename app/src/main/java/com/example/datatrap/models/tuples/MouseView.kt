package com.example.datatrap.models.tuples

data class MouseView(
    var mouseIid: Long,
    var deviceID: String,
    val body: Float?,
    val tail: Float?,
    val feet: Float?,
    val ear: Float?,
    val mouseCaught: Long,
    val gravidity: Boolean?,
    val lactating: Boolean?,
    val sexActive: Boolean,
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
