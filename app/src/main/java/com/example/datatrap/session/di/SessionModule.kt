package com.example.datatrap.session.di

import com.example.datatrap.core.data.db.TrapDatabase
import com.example.datatrap.session.data.SessionDao
import com.example.datatrap.session.data.SessionRepository
import com.example.datatrap.session.domain.DeleteSessionUseCase
import com.example.datatrap.session.presentation.session_edit.SessionViewModel
import com.example.datatrap.session.presentation.session_list.SessionListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val sessionModule = module {
    single<SessionDao> {
        val database = get<TrapDatabase>()
        database.sessionDao()
    }

    single { SessionRepository(get()) }

    single {
        DeleteSessionUseCase(get(), get())
    }

    viewModel {param ->
        SessionListViewModel(get(), get(), get(), get(), get(), param[0], param[1])
    }
    viewModel {param ->
        SessionViewModel(get(), param[0], param[1])
    }
}