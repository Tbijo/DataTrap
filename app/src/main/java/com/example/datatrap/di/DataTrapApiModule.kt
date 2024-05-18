package com.example.datatrap.di

import com.example.datatrap.core.util.Constants.SERVER_URL
import com.example.datatrap.sync.data.remote.DataTrapAPI
import com.example.datatrap.sync.data.remote.DataTrapRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataTrapApiModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DataTrapAPI::class.java)
    }

    single {
        DataTrapRepository(get())
    }
}