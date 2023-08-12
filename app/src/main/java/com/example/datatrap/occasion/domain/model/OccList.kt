package com.example.datatrap.occasion.domain.model

data class OccList(
    val occasionId: String,
    val localityID: Long,
    val occasion: Int,
    val occasionStart: Long,
    val numMice: Int?,
    val numTraps: Int
)
