package com.example.datatrap.occasion.data.weather

import com.example.datatrap.occasion.data.weather.model.current_weather.CurrentWeatherDto
import com.example.datatrap.occasion.data.weather.model.history_weather.HistoryWeatherDto
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherAPI {

    @GET("weather?lat={latitude}&lon={longitude}&units=metric&appid=ba1d923dba4826c58fda121fb5e7a9de")
    suspend fun getCurrentWeather(
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double,
    ): CurrentWeatherDto

    @GET("onecall/timemachine?lat={latitude}&lon={longitude}&dt={unixtime}&units=metric&only_current=true&appid=ba1d923dba4826c58fda121fb5e7a9de")
    suspend fun getHistoryWeather(
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double,
        @Path("unixtime") unixTime: Long,
    ): HistoryWeatherDto
}