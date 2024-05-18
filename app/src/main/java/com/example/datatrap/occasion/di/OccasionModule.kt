package com.example.datatrap.occasion.di

import com.example.datatrap.camera.data.occasion_image.OccasionImageDao
import com.example.datatrap.camera.data.occasion_image.OccasionImageRepository
import com.example.datatrap.core.data.db.TrapDatabase
import com.example.datatrap.occasion.data.occasion.OccasionDao
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.occasion.domain.use_case.CountSpecialSpeciesUseCase
import com.example.datatrap.occasion.domain.use_case.DeleteOccasionUseCase
import com.example.datatrap.occasion.domain.use_case.GetWeatherUseCase
import com.example.datatrap.occasion.domain.use_case.InsertOccasionUseCase
import com.example.datatrap.occasion.presentation.occasion_add_edit.OccasionViewModel
import com.example.datatrap.occasion.presentation.occasion_detail.OccasionDetailViewModel
import com.example.datatrap.occasion.presentation.occasion_list.OccasionListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val occasionModule = module {
    single<OccasionDao> {
        val database = get<TrapDatabase>()
        database.occasionDao()
    }
    single<OccasionImageDao> {
        val database = get<TrapDatabase>()
        database.occasionImageDao()
    }

    single { OccasionRepository(get()) }
    single { OccasionImageRepository(get()) }

    single {
        GetWeatherUseCase(get())
    }
    single {
        InsertOccasionUseCase(get(), get(), get(), get())
    }
    single {
        DeleteOccasionUseCase(get(), get(), get(), get(), get())
    }
    single {
        CountSpecialSpeciesUseCase(get(), get())
    }

    viewModel {param ->
        OccasionListViewModel(get(), get(), get(), get(), get(), param[0], param[1])
    }
    viewModel {param ->
        OccasionViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), param[0], param[1], param[2])
    }
    viewModel {param ->
        OccasionDetailViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), param[0], param[1])
    }
}