package com.example.datatrap.di

import com.example.datatrap.utils.Constants.BASE_URL
import com.example.datatrap.www.DataTrapAPI
import com.example.datatrap.www.DataTrapRepository
import dagger.Provides
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitModule {

    @ActivityRetainedScoped
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @ActivityRetainedScoped
    @Provides
    fun provideSimpleApi(retrofit: Retrofit): DataTrapAPI {
        return retrofit.create(DataTrapAPI::class.java)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideRepository(
        api: DataTrapAPI
    ): DataTrapRepository =
        DataTrapRepository(api)

}