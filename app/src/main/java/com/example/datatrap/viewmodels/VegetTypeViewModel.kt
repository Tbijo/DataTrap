package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.VegetType
import com.example.datatrap.repositories.VegetTypeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VegetTypeViewModel(application: Application): AndroidViewModel(application) {

    val vegetTypeList: LiveData<List<VegetType>>
    private val vegetTypeRepository: VegetTypeRepository

    init {
        val vegetTypeDao = TrapDatabase.getDatabase(application).vegetTypeDao()
        vegetTypeRepository = VegetTypeRepository(vegetTypeDao)
        vegetTypeList = vegetTypeRepository.vegetTypeList
    }

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