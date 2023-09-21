package com.example.datatrap.di

import com.example.datatrap.core.util.Constants.SERVER_URL
import com.example.datatrap.sync.data.remote.DataTrapAPI
import com.example.datatrap.sync.data.remote.DataTrapRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ActivityRetainedComponent::class)
object DataTrapApiModule {

    @ActivityRetainedScoped
    @Provides
    fun provideDataTrapApi(): DataTrapAPI {
        return Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DataTrapAPI::class.java)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideDataTrapRepository(
        api: DataTrapAPI
    ): DataTrapRepository = DataTrapRepository(api)

}