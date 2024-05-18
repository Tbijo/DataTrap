package com.example.datatrap.di

import androidx.room.Room
import com.example.datatrap.core.data.db.TrapDatabase
import org.koin.dsl.module

val roomDatabaseModule = module {
    single {
        Room.databaseBuilder(
            context = get(),
            klass = TrapDatabase::class.java,
            name = "trap_database",
        )
            .createFromAsset("database/init_database.db")
            .build()
    }
}