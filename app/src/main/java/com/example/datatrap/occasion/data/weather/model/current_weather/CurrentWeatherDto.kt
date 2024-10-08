package com.example.datatrap.occasion.data.weather.model.current_weather

import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherDto(
    val base: String?,
    val clouds: Clouds?,
    val cod: Int?,
    val coord: Coord?,
    val dt: Int?,
    val id: Int?,
    val main: Main?,
    val name: String?,
    val sys: Sys?,
    val timezone: Int?,
    val visibility: Int?,
    val weather: List<Weather?>?,
    val wind: Wind?
)