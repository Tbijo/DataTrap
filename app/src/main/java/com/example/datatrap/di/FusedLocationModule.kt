package com.example.datatrap.di

import android.content.Context
import com.example.datatrap.locality.data.location.LocationClientImpl
import com.example.datatrap.locality.domain.LocationClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object FusedLocationModule {

    @ActivityRetainedScoped
    @Provides
    fun provideFusedLocation(context: Context) = LocationServices.getFusedLocationProviderClient(context)

    @ActivityRetainedScoped
    @Provides
    fun provideLocationClient(context: Context, client: FusedLocationProviderClient): LocationClient {
        return LocationClientImpl(context, client)
    }
}