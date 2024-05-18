package com.example.datatrap.settings.di

import com.example.datatrap.core.data.db.TrapDatabase
import com.example.datatrap.settings.data.env_type.EnvTypeDao
import com.example.datatrap.settings.data.env_type.EnvTypeRepository
import com.example.datatrap.settings.data.method.MethodDao
import com.example.datatrap.settings.data.method.MethodRepository
import com.example.datatrap.settings.data.methodtype.MethodTypeDao
import com.example.datatrap.settings.data.methodtype.MethodTypeRepository
import com.example.datatrap.settings.data.protocol.ProtocolDao
import com.example.datatrap.settings.data.protocol.ProtocolRepository
import com.example.datatrap.settings.data.traptype.TrapTypeDao
import com.example.datatrap.settings.data.traptype.TrapTypeRepository
import com.example.datatrap.settings.data.veg_type.VegetTypeDao
import com.example.datatrap.settings.data.veg_type.VegetTypeRepository
import com.example.datatrap.settings.presentation.SettingsEntityViewModel
import com.example.datatrap.settings.user.data.UserDao
import com.example.datatrap.settings.user.data.UserRepository
import com.example.datatrap.settings.user.presentation.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single<EnvTypeDao> {
        val database = get<TrapDatabase>()
        database.envTypeDao()
    }
    single<MethodDao> {
        val database = get<TrapDatabase>()
        database.methodDao()
    }
    single<MethodTypeDao> {
        val database = get<TrapDatabase>()
        database.methodTypeDao()
    }
    single<ProtocolDao> {
        val database = get<TrapDatabase>()
        database.protocolDao()
    }
    single<TrapTypeDao> {
        val database = get<TrapDatabase>()
        database.trapTypeDao()
    }
    single<VegetTypeDao> {
        val database = get<TrapDatabase>()
        database.vegetTypeDao()
    }
    single<UserDao> {
        val database = get<TrapDatabase>()
        database.userDao()
    }

    single { EnvTypeRepository(get()) }
    single { MethodRepository(get()) }
    single { MethodTypeRepository(get()) }
    single { ProtocolRepository(get()) }
    single { TrapTypeRepository(get()) }
    single { VegetTypeRepository(get()) }
    single { UserRepository(get()) }

    viewModel {param ->
        SettingsEntityViewModel(get(), get(), get(), get(), get(), get(), param[0])
    }
    viewModel { UserViewModel(get()) }
}