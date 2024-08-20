package com.example.datatrap.di

import com.example.datatrap.occasion.data.weather.WeatherRepository
import com.example.datatrap.sync.data.remote.DataTrapRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val dataTrapApiModule = module {
    single {
        HttpClient(OkHttp.create()) {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }

    single {
        WeatherRepository(get())
    }

    single {
        DataTrapRepository(get())
    }
}