package com.example.datatrap.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.datatrap.core.data.shared_nav_args.NavArgsStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val SHARE_PREF_NAME = "myNavArgs"

@Module
@InstallIn(SingletonComponent::class)
object NavArgsModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideNavArgsStorage(sharedPreferences: SharedPreferences): NavArgsStorage {
        return NavArgsStorage(sharedPreferences)
    }

}