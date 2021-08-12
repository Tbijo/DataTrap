package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.EnvType
import com.example.datatrap.repositories.EnvTypeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EnvTypeViewModel(application: Application): AndroidViewModel(application) {

    val envTypeList: Flow<List<EnvType>>
    private val envTypeRepository: EnvTypeRepository

    init {
        val envTypeDao = TrapDatabase.getDatabase(application).envTypeDao()
        envTypeRepository = EnvTypeRepository(envTypeDao)
        envTypeList = envTypeRepository.envTypeList
    }

    fun insertEnvType(envType: EnvType){
        viewModelScope.launch(Dispatchers.IO) {
            envTypeRepository.insertEnvType(envType)
        }
    }

    fun updateEnvType(envType: EnvType){
        viewModelScope.launch(Dispatchers.IO) {
            envTypeRepository.updateEnvType(envType)
        }
    }

    fun deleteEnvType(envType: EnvType){
        viewModelScope.launch(Dispatchers.IO) {
            envTypeRepository.deleteEnvType(envType)
        }
    }
}