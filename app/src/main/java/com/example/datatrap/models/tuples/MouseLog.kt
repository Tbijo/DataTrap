package com.example.datatrap.models.tuples

import java.util.*

data class MouseLog(
    val mouseDateTimeCreated: Date,
    val localityName: String,
    val trapID: Int?,
    val weight: Float?,
    val sexActive: Boolean,
    val gravidity: Boolean?,
    val lactating: Boolean?
)
