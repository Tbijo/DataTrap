package com.example.datatrap.occasion.data.weather

import com.example.datatrap.core.util.Constants
import com.example.datatrap.occasion.data.weather.model.current_weather.CurrentWeatherDto
import com.example.datatrap.occasion.data.weather.model.history_weather.HistoryWeatherDto
import com.example.datatrap.sync.utils.NetworkError
import com.example.datatrap.sync.utils.Result
import com.example.datatrap.sync.utils.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType

class WeatherRepository(
    private val httpClient: HttpClient,
) {
    // https://api.openweathermap.org/data/2.5/weather?lat=41.85&lon=-87.65&units=metric&appid=ba1d923dba4826c58fda121fb5e7a9de

    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
    ): Result<CurrentWeatherDto, NetworkError> =
        safeApiCall<CurrentWeatherDto> {
            httpClient.get(
                urlString = "${Constants.WEATHER_URL}weather"
            ) {
                parameter("lat", latitude)
                parameter("lon", longitude)
                parameter("units", "metric")
                parameter("appid", "ba1d923dba4826c58fda121fb5e7a9de")
                contentType(ContentType.Application.Json)
            }
        }


    // https://api.openweathermap.org/data/2.5/onecall/timemachine?lat=41.85&lon=-87.65&dt=1694819106&units=metric&only_current=true&appid=ba1d923dba4826c58fda121fb5e7a9de

    suspend fun getHistoryWeather(
        latitude: Double,
        longitude: Double,
        unixTime: Long,
    ): Result<HistoryWeatherDto, NetworkError> =
        safeApiCall<HistoryWeatherDto> {
            httpClient.get(
                urlString = "${Constants.WEATHER_URL}onecall/timemachine"
            ) {
                parameter("lat", latitude)
                parameter("lon", longitude)
                parameter("dt", unixTime)
                parameter("units", "metric")
                parameter("only_current", "true")
                parameter("appid", "ba1d923dba4826c58fda121fb5e7a9de")
                contentType(ContentType.Application.Json)
            }
        }

}