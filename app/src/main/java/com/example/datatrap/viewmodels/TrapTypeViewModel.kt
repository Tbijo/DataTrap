package com.example.datatrap.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.models.TrapType
import com.example.datatrap.repositories.TrapTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrapTypeViewModel @Inject constructor(
    private val trapTypeRepository: TrapTypeRepository
): ViewModel() {

    val trapTypeList: LiveData<List<TrapType>> = trapTypeRepository.trapTypeList

    fun insertTrapType(trapType: TrapType) {
        viewModelScope.launch(Dispatchers.IO) {
            trapTypeRepository.insertTrapType(trapType)
        }
    }

    fun updateTrapType(trapType: TrapType) {
        viewModelScope.launch(Dispatchers.IO) {
            trapTypeRepository.updateTrapType(trapType)
        }
    }

    fun deleteTrapType(trapType: TrapType) {
        viewModelScope.launch(Dispatchers.IO) {
            trapTypeRepository.deleteTrapType(trapType)
        }
    }
}