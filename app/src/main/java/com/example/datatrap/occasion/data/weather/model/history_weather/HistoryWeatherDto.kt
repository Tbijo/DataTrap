package com.example.datatrap.occasion.data.weather.model.history_weather

import kotlinx.serialization.Serializable

@Serializable
data class HistoryWeatherDto(
    val current: Current?,
    val lat: Double?,
    val lon: Double?,
    val timezone: String?,
    val timezone_offset: Int?
)