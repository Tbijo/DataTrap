package com.example.datatrap.specie.di

import com.example.datatrap.core.data.db.TrapDatabase
import com.example.datatrap.specie.data.SpecieDao
import com.example.datatrap.specie.data.SpecieRepository
import com.example.datatrap.specie.data.specie_image.SpecieImageDao
import com.example.datatrap.specie.data.specie_image.SpecieImageRepository
import com.example.datatrap.specie.domain.use_case.DeleteSpecieUseCase
import com.example.datatrap.specie.domain.use_case.InsertSpecieUseCase
import com.example.datatrap.specie.presentation.specie_add_edit.SpecieViewModel
import com.example.datatrap.specie.presentation.specie_detail.SpecieDetailViewModel
import com.example.datatrap.specie.presentation.specie_image.SpecieImageViewModel
import com.example.datatrap.specie.presentation.specie_list.SpecieListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val specieModule = module {
    single<SpecieDao> {
        val database = get<TrapDatabase>()
        database.specieDao()
    }
    single<SpecieImageDao> {
        val database = get<TrapDatabase>()
        database.specieImageDao()
    }

    single { SpecieRepository(get()) }
    single { SpecieImageRepository(get()) }

    single {
        InsertSpecieUseCase(get(), get())
    }
    single {
        DeleteSpecieUseCase(get(), get())
    }

    viewModel { SpecieListViewModel(get(), get()) }
    viewModel { param ->
        SpecieViewModel(get(), get(), get(), param[0])
    }
    viewModel {param ->
        SpecieImageViewModel(get(), param[0])
    }
    viewModel {param ->
        SpecieDetailViewModel(get(), get(), param[0])
    }
}