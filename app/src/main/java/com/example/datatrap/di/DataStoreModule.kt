package com.example.datatrap.di

import android.content.Context
import com.example.datatrap.core.data.pref.PrefRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun providePathPrefRep(
        @ApplicationContext context: Context
    ) = PrefRepository(context)

}