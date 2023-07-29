package com.example.datatrap.occasion.data

data class OccasionView(

    val occasionNum: Int,

    val locality: String,

    val method: String,

    val methodType: String,

    val trapType: String,

    val envType: String?,

    val vegetType: String?,

    val occasionStart: Long,

    val gotCaught: Boolean?,

    val numTraps: Int,

    val numMice: Int?,

    val temperature: Float?,

    val weather: String?,

    val leg: String,

    val note: String?
)
