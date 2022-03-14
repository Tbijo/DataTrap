package com.example.datatrap.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.models.MethodType
import com.example.datatrap.repositories.MethodTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MethodTypeViewModel @Inject constructor(
    private val methodTypeRepository: MethodTypeRepository
) : ViewModel() {

    val methodTypeList: LiveData<List<MethodType>> = methodTypeRepository.methodTypeList

    fun insertMethodType(methodType: MethodType) {
        viewModelScope.launch(Dispatchers.IO) {
            methodTypeRepository.insertMethodType(methodType)
        }
    }

    fun updateMethodType(methodType: MethodType) {
        viewModelScope.launch(Dispatchers.IO) {
            methodTypeRepository.updateMethodType(methodType)
        }
    }

    fun deleteMethodType(methodType: MethodType) {
        viewModelScope.launch(Dispatchers.IO) {
            methodTypeRepository.deleteMethodType(methodType)
        }
    }
}