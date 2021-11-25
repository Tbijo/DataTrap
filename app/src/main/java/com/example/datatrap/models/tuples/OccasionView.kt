package com.example.datatrap.models.tuples

import java.util.*

data class OccasionView(

    val occasionNum: Int,

    val locality: String,

    val method: String,

    val methodType: String,

    val trapType: String,

    val envType: String?,

    val vegetType: String?,

    val dateTime: Date,

    val gotCaught: Boolean?,

    val numTraps: Int,

    val numMice: Int?,

    val temperature: Float?,

    val weather: String?,

    val leg: String,

    val note: String?
)
