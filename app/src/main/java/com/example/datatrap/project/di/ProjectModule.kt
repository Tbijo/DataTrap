package com.example.datatrap.project.di

import com.example.datatrap.core.data.db.TrapDatabase
import com.example.datatrap.project.data.ProjectDao
import com.example.datatrap.project.data.ProjectRepository
import com.example.datatrap.project.presentation.project_edit.ProjectViewModel
import com.example.datatrap.project.presentation.project_list.ProjectListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val projectModule = module {
    single<ProjectDao> {
        val database = get<TrapDatabase>()
        database.projectDao()
    }

    single { ProjectRepository(get()) }

    viewModel { ProjectListViewModel(get()) }
    viewModel {param ->
        ProjectViewModel(get(), param[0])
    }
}