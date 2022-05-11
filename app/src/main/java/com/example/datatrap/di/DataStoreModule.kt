package com.example.datatrap.di

import android.content.Context
import com.example.datatrap.repositories.datastore.PathPrefRepository
import com.example.datatrap.repositories.datastore.UserPrefRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object DataStoreModule {

    @ActivityRetainedScoped
    @Provides
    fun provideUserPrefRep(
        @ApplicationContext context: Context
    ) = UserPrefRepository(context)

    @ActivityRetainedScoped
    @Provides
    fun providePathPrefRep(
        @ApplicationContext context: Context
    ) = PathPrefRepository(context)

}