package com.example.datatrap.occasion.data.weather.model.current_weather

import kotlinx.serialization.Serializable

@Serializable
data class Coord(
    val lat: Double?,
    val lon: Double?
)