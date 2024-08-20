package com.example.datatrap.occasion.data.weather.model.current_weather

import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    val description: String?,
    val icon: String?,
    val id: Int?,
    val main: String?
)