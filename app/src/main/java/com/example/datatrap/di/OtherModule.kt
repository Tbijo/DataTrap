package com.example.datatrap.di

import android.content.Context
import com.example.datatrap.core.data.db.TrapDatabase
import com.example.datatrap.core.data.locality_session.LocalitySessionDao
import com.example.datatrap.core.data.locality_session.LocalitySessionRepository
import com.example.datatrap.core.data.project_locality.ProjectLocalityDao
import com.example.datatrap.core.data.project_locality.ProjectLocalityRepository
import com.example.datatrap.core.data.shared_nav_args.ScreenNavArgs
import com.example.datatrap.core.data.storage.InternalStorageRepository
import com.example.datatrap.core.domain.use_case.DeleteLocalitySessionUseCase
import com.example.datatrap.core.domain.use_case.DeleteProjectLocalityUseCase
import com.example.datatrap.core.domain.use_case.InsertLocalitySessionUseCase
import com.example.datatrap.core.domain.use_case.InsertProjectLocalityUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

const val SHARE_PREF_NAME = "myNavArgs"

val otherModule = module {
    single { androidContext().getSharedPreferences(SHARE_PREF_NAME, Context.MODE_PRIVATE) }
    single { ScreenNavArgs(get()) }

    single<LocalitySessionDao> {
        val database = get<TrapDatabase>()
        database.localitySessionDao()
    }
    single<ProjectLocalityDao> {
        val database = get<TrapDatabase>()
        database.projectLocalityDao()
    }

    single { ProjectLocalityRepository(get()) }
    single { LocalitySessionRepository(get()) }
    single { InternalStorageRepository(get()) }

    single { InsertProjectLocalityUseCase(get(), get()) }
    single { DeleteProjectLocalityUseCase(get(), get()) }
    single { InsertLocalitySessionUseCase(get(), get()) }
    single { DeleteLocalitySessionUseCase(get(), get()) }
}