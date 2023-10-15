package com.example.datatrap.di

import android.content.Context
import androidx.room.Room
import com.example.datatrap.core.data.db.TrapDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBmodule {

    @Singleton
    @Provides
    fun provideTrapDatabase(
        @ApplicationContext context: Context
    ): TrapDatabase {
        return Room.databaseBuilder(
            context,
            TrapDatabase::class.java,
            "trap_database"
        )
            .createFromAsset("database/init_database.db")
            .build()
    }

}