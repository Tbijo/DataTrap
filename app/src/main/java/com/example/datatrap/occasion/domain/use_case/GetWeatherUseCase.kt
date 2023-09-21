package com.example.datatrap.occasion.domain.use_case

import com.example.datatrap.core.util.Constants
import com.example.datatrap.core.util.Resource
import com.example.datatrap.occasion.data.occasion.OccasionEntity
import com.example.datatrap.occasion.data.weather.WeatherRepository
import com.example.datatrap.occasion.data.weather.model.current_weather.CurrentWeatherDto
import com.example.datatrap.occasion.data.weather.model.history_weather.HistoryWeatherDto
import com.example.datatrap.occasion.domain.model.MyWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.util.Calendar

class GetWeatherUseCase(
    private val weatherRepository: WeatherRepository,
) {
    operator fun invoke(
        occasionEntity: OccasionEntity?,
        latitude: Double?,
        longitude: Double?,
    ): Flow<Resource<MyWeather>> = flow {
        if (latitude == null || longitude == null) {
            emit(Resource.Error(throwable = Exception("No coordinates for this locality.")))
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
                unixTime = occasionEntity.occasionStart
            )
            emit(weather)
        }
    }

    private suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
    ): Resource<MyWeather> {
        return try {
            val currentWeather: CurrentWeatherDto = weatherRepository.getCurrentWeather(
                latitude = latitude,
                longitude = longitude,
            )

            val temp = currentWeather.main?.temp
            val weather = currentWeather.weather?.first()?.main

            Resource.Success(
                data = MyWeather(temp, weather),
            )
        } catch (e: IOException) {
            Resource.Error(e)
        } catch (e: HttpException) {
            Resource.Error(e)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    private suspend fun getHistoryWeather(
        latitude: Double,
        longitude: Double,
        unixTime: Long,
    ): Resource<MyWeather> {
        return try {
            val currentTime = Calendar.getInstance().time.time
            if (currentTime - unixTime >= Constants.SECONDS_IN_FIVE_DAYS) {
                Resource.Error<MyWeather>(Exception("Occasion is older than 5 days."))
            }

            val historyWeather: HistoryWeatherDto = weatherRepository.getHistoryWeather(
                latitude = latitude,
                longitude = longitude,
                unixTime = unixTime,
            )

            val temp = historyWeather.current?.temp
            val weather = historyWeather.current?.weather?.first()?.main

            Resource.Success(
                data = MyWeather(temp, weather),
            )
        } catch (e: IOException) {
            Resource.Error(e)
        } catch (e: HttpException) {
            Resource.Error(e)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}