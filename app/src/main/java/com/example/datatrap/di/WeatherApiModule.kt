package com.example.datatrap.di

import com.example.datatrap.core.util.Constants
import com.example.datatrap.occasion.data.weather.WeatherAPI
import com.example.datatrap.occasion.data.weather.WeatherRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val weatherApiModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(Constants.WEATHER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherAPI::class.java)
    }

    single {
        WeatherRepository(get())
    }
}