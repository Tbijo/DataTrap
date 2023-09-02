package com.example.datatrap.di

import com.example.datatrap.core.util.Constants.BASE_URL
import com.example.datatrap.sync.data.DataTrapAPI
import com.example.datatrap.sync.data.DataTrapRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ActivityRetainedComponent::class)
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