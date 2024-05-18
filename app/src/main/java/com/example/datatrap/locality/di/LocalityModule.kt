package com.example.datatrap.locality.di

import com.example.datatrap.core.data.db.TrapDatabase
import com.example.datatrap.locality.data.locality.LocalityDao
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.locality.data.location.LocationClientImpl
import com.example.datatrap.locality.presentation.locality_add_edit.LocalityViewModel
import com.example.datatrap.locality.presentation.locality_list.LocalityListViewModel
import com.example.datatrap.locality.presentation.locality_map.LocalityMapViewModel
import com.google.android.gms.location.LocationServices
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val localityModule = module {
    single<LocalityDao> {
        val database = get<TrapDatabase>()
        database.localityDao()
    }
    single {
        LocationServices.getFusedLocationProviderClient(get())
    }
    single {
        LocationClientImpl(get(), get())
    }

    single { LocalityRepository(get()) }

    viewModel {param ->
        LocalityListViewModel(get(), get(), param[0])
    }
    viewModel {param ->
        LocalityViewModel(get(), get(), param[0])
    }
    viewModel {
        LocalityMapViewModel(get())
    }
}