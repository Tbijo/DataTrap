package com.example.datatrap.occasion.data.weather

import com.example.datatrap.occasion.data.weather.model.current_weather.CurrentWeatherDto
import com.example.datatrap.occasion.data.weather.model.history_weather.HistoryWeatherDto

class WeatherRepository(
    private val api: WeatherAPI
) {

    // https://api.openweathermap.org/data/2.5/weather?lat=41.85&lon=-87.65&units=metric&appid=ba1d923dba4826c58fda121fb5e7a9de

    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
    ): CurrentWeatherDto {
        return api.getCurrentWeather(
            latitude = latitude,
            longitude = longitude,
        )
    }

    // https://api.openweathermap.org/data/2.5/onecall/timemachine?lat=41.85&lon=-87.65&dt=1694819106&units=metric&only_current=true&appid=ba1d923dba4826c58fda121fb5e7a9de

    suspend fun getHistoryWeather(
        latitude: Double,
        longitude: Double,
        unixTime: Long,
    ): HistoryWeatherDto {
        return api.getHistoryWeather(
            latitude = latitude,
            longitude = longitude,
            unixTime = unixTime,
        )
    }

}