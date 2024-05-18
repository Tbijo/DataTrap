package com.example.datatrap.camera.di

import com.example.datatrap.camera.presentation.CameraViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cameraModule = module {
    viewModel { param ->
        CameraViewModel(
            mouseImageRepository = get(),
            occasionImageRepository = get(),
            internalStorageRepository = get(),
            entity = param[0],
            entityId = param[1],
        )
    }
}