package com.example.datatrap.occasion.data.weather.model.current_weather

import kotlinx.serialization.Serializable

@Serializable
data class Wind(
    val deg: Int?,
    val gust: Double?,
    val speed: Double?
)