package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.TrapType
import com.example.datatrap.repositories.TrapTypeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TrapTypeViewModel(application: Application): AndroidViewModel(application) {

    val trapTypeList: LiveData<List<TrapType>>
    private val trapTypeRepository: TrapTypeRepository

    init {
        val trapTypeDao = TrapDatabase.getDatabase(application).trapTypeDao()
        trapTypeRepository = TrapTypeRepository(trapTypeDao)
        trapTypeList = trapTypeRepository.trapTypeList
    }

    fun insertTrapType(trapType: TrapType){
        viewModelScope.launch(Dispatchers.IO) {
            trapTypeRepository.insertTrapType(trapType)
        }
    }

    fun updateTrapType(trapType: TrapType){
        viewModelScope.launch(Dispatchers.IO) {
            trapTypeRepository.updateTrapType(trapType)
        }
    }

    fun deleteTrapType(trapType: TrapType){
        viewModelScope.launch(Dispatchers.IO) {
            trapTypeRepository.deleteTrapType(trapType)
        }
    }
}