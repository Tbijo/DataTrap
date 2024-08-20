package com.example.datatrap.sync.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class MouseImageSync(
    val imgName: String,
    val path: String,
    val note: String?,
    val mouseIiD: Long,
    val deviceID: String,
    val uniqueCode: Long
)

@Serializable
data class OccasionImageSync(
    val imgName: String,
    val path: String,
    val note: String?,
    val occasionID: Long,
    val uniqueCode: Long
)

@Serializable
data class SpecieImageSync(
    val imgName: String,
    val path: String,
    val note: String?,
    val specieID: Long,
    val uniqueCode: Long
)

@Serializable
data class ImageSync(
    val mouseImages: List<MouseImageSync>,
    val occasionImages: List<OccasionImageSync>,
    val specieImages: List<SpecieImageSync>
)