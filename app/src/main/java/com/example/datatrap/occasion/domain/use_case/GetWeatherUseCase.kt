package com.example.datatrap.occasion.domain.use_case

import com.example.datatrap.core.util.Constants
import com.example.datatrap.occasion.data.occasion.OccasionEntity
import com.example.datatrap.occasion.data.weather.WeatherRepository
import com.example.datatrap.occasion.domain.model.MyWeather
import com.example.datatrap.sync.utils.NetworkError
import com.example.datatrap.sync.utils.Result
import com.example.datatrap.sync.utils.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar

class GetWeatherUseCase(
    private val weatherRepository: WeatherRepository,
) {
    operator fun invoke(
        occasionEntity: OccasionEntity?,
        latitude: Double?,
        longitude: Double?,
    ): Flow<Result<MyWeather, NetworkError>> = flow {
        if (latitude == null || longitude == null) {
            // No coordinates for this locality
            emit(Result.Error(NetworkError.MISSING_DATA))
            return@flow
        }

        if (occasionEntity == null) {
            val weather = getCurrentWeather(
                latitude = latitude,
                longitude = longitude,
            )
            emit(weather)
        } else {
            val weather = getHistoryWeather(
                latitude = latitude,
                longitude = longitude,
                unixTime = occasionEntity.occasionStart.toEpochSecond()
            )
            emit(weather)
        }
    }

    private suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
    ): Result<MyWeather, NetworkError> {
        val result = weatherRepository.getCurrentWeather(
            latitude = latitude,
            longitude = longitude,
        )

        return result.map {
            val temp = it.main?.temp
            val weather = it.weather?.first()?.main

            MyWeather(temp, weather)
        }
    }

    private suspend fun getHistoryWeather(
        latitude: Double,
        longitude: Double,
        unixTime: Long,
    ): Result<MyWeather, NetworkError> {
        val currentTime = Calendar.getInstance().time.time
        if (currentTime - unixTime >= Constants.SECONDS_IN_FIVE_DAYS) {
            // Occasion is older than 5 days
            return Result.Error(NetworkError.API_ERROR)
        }

        val result = weatherRepository.getHistoryWeather(
            latitude = latitude,
            longitude = longitude,
            unixTime = unixTime,
        )

        return result.map {
            val temp = it.current?.temp
            val weather = it.current?.weather?.first()?.main

            MyWeather(temp, weather)
        }
    }
}