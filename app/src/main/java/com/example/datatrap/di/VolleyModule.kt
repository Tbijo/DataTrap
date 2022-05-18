package com.example.datatrap.di

import android.content.Context
import com.android.volley.toolbox.Volley
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object VolleyModule {

    @ViewModelScoped
    @Provides
    fun provideVolley(
        @ApplicationContext context: Context
    ) = Volley.newRequestQueue(context.applicationContext)

}