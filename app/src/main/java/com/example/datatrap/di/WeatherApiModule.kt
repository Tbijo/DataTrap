package com.example.datatrap.di

import com.example.datatrap.core.util.Constants
import com.example.datatrap.occasion.data.weather.WeatherAPI
import com.example.datatrap.occasion.data.weather.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ActivityRetainedComponent::class)
object WeatherApiModule {

    @ActivityRetainedScoped
    @Provides
    fun provideWeatherApi(): WeatherAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.WEATHER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherAPI::class.java)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideWeatherRepository(
        api: WeatherAPI,
    ): WeatherRepository = WeatherRepository(api)
}