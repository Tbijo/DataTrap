package com.example.datatrap.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.models.VegetType
import com.example.datatrap.repositories.VegetTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VegetTypeViewModel @Inject constructor(
    private val vegetTypeRepository: VegetTypeRepository
): ViewModel() {

    val vegetTypeList: LiveData<List<VegetType>> = vegetTypeRepository.vegetTypeList

    fun insertVegetType(vegetType: VegetType) {
        viewModelScope.launch(Dispatchers.IO) {
            vegetTypeRepository.insertVegetType(vegetType)
        }
    }

    fun updateVegetType(vegetType: VegetType) {
        viewModelScope.launch(Dispatchers.IO) {
            vegetTypeRepository.updateVegetType(vegetType)
        }
    }

    fun deleteVegetType(vegetType: VegetType) {
        viewModelScope.launch(Dispatchers.IO) {
            vegetTypeRepository.deleteVegetType(vegetType)
        }
    }
}