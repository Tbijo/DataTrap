package com.example.datatrap.mouse.di

import com.example.datatrap.camera.data.mouse_image.MouseImageDao
import com.example.datatrap.camera.data.mouse_image.MouseImageRepository
import com.example.datatrap.core.data.db.TrapDatabase
import com.example.datatrap.core.domain.use_case.GetInfoNamesUseCase
import com.example.datatrap.mouse.data.MouseDao
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.mouse.domain.use_case.DeleteMouseUseCase
import com.example.datatrap.mouse.domain.use_case.GenerateCodeUseCase
import com.example.datatrap.mouse.domain.use_case.GetMiceByOccasion
import com.example.datatrap.mouse.domain.use_case.GetMiceForRecapture
import com.example.datatrap.mouse.domain.use_case.GetMouseDetail
import com.example.datatrap.mouse.domain.use_case.GetOccupiedTrapIdsInOccasion
import com.example.datatrap.mouse.domain.use_case.GetPreviousLogsOfMouse
import com.example.datatrap.mouse.domain.use_case.InsertMouseUseCase
import com.example.datatrap.mouse.presentation.mouse_add_edit.MouseViewModel
import com.example.datatrap.mouse.presentation.mouse_add_multi.MouseMultiViewModel
import com.example.datatrap.mouse.presentation.mouse_detail.MouseDetailViewModel
import com.example.datatrap.mouse.presentation.mouse_list.MouseListViewModel
import com.example.datatrap.mouse.presentation.mouse_recapture_list.RecaptureListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mouseModule = module {
    single<MouseDao> {
        val database = get<TrapDatabase>()
        database.mouseDao()
    }
    single<MouseImageDao> {
        val database = get<TrapDatabase>()
        database.mouseImageDao()
    }

    single { MouseRepository(get()) }
    single { MouseImageRepository(get()) }

    single {
        InsertMouseUseCase(get(), get(), get(), get(), get(), get(), get())
    }
    single {
        GetMouseDetail(get(), get(), get(), get(), get())
    }
    single {
        GetMiceForRecapture(get(), get(), get())
    }
    single {
        GetPreviousLogsOfMouse(get(), get())
    }
    single {
        GetMiceByOccasion(get(), get())
    }
    single {
        DeleteMouseUseCase(get(), get(), get(), get(), get(), get())
    }
    single {
        GenerateCodeUseCase(get(), get())
    }
    single {
        GetOccupiedTrapIdsInOccasion(get())
    }
    single {
        GetInfoNamesUseCase(get(), get(), get(), get())
    }

    viewModel {param ->
        MouseListViewModel(get(), get(), get(), get(), param[0], param[1], param[2])
    }
    viewModel {param ->
        MouseViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), param[0], param[1], param[2], param[3])
    }
    viewModel {param ->
        MouseDetailViewModel(get(), get(), get(), param[0])
    }
    viewModel {param ->
        MouseMultiViewModel(get(), get(), get(), param[0], param[1])
    }
    viewModel { RecaptureListViewModel(get(), get()) }
}