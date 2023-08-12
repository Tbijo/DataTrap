package com.example.datatrap.sync.data

data class MouseImageSync(
    var imgName: String,
    var path: String,
    var note: String?,
    var mouseIiD: Long,
    var deviceID: String,
    var uniqueCode: Long
)

data class OccasionImageSync(
    var imgName: String,
    var path: String,
    var note: String?,
    var occasionID: Long,
    var uniqueCode: Long
)

data class SpecieImageSync(
    var imgName: String,
    var path: String,
    var note: String?,
    var specieID: Long,
    var uniqueCode: Long
)

data class ImageSync(
    var mouseImages: List<MouseImageSync>,
    var occasionImages: List<OccasionImageSync>,
    var specieImages: List<SpecieImageSync>
)