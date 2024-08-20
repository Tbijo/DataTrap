package com.example.datatrap.occasion.data.weather.model.current_weather

import kotlinx.serialization.Serializable

@Serializable
data class Main(
    val feels_like: Double?,
    val humidity: Int?,
    val pressure: Int?,
    val temp: Double?,
    val temp_max: Double?,
    val temp_min: Double?
)