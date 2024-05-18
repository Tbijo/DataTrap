package com.example.datatrap.login.di

import com.example.datatrap.login.presentation.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    viewModel {
        LoginViewModel(get(), get())
    }
}