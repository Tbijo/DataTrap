package com.example.datatrap.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.models.EnvType
import com.example.datatrap.repositories.EnvTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnvTypeViewModel @Inject constructor (
    private val envTypeRepository: EnvTypeRepository
) : ViewModel() {

    val envTypeList: LiveData<List<EnvType>> = envTypeRepository.envTypeList

    fun insertEnvType(envType: EnvType) {
        viewModelScope.launch(Dispatchers.IO) {
            envTypeRepository.insertEnvType(envType)
        }
    }

    fun updateEnvType(envType: EnvType) {
        viewModelScope.launch(Dispatchers.IO) {
            envTypeRepository.updateEnvType(envType)
        }
    }

    fun deleteEnvType(envType: EnvType) {
        viewModelScope.launch(Dispatchers.IO) {
            envTypeRepository.deleteEnvType(envType)
        }
    }
}