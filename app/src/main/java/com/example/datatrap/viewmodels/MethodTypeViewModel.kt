package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.MethodType
import com.example.datatrap.repositories.MethodTypeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MethodTypeViewModel(application: Application): AndroidViewModel(application) {

    val methodTypeList: LiveData<List<MethodType>>
    private val methodTypeRepository: MethodTypeRepository

    init {
        val methodTypeDao = TrapDatabase.getDatabase(application).methodTypeDao()
        methodTypeRepository = MethodTypeRepository(methodTypeDao)
        methodTypeList = methodTypeRepository.methodTypeList
    }

    fun insertMethodType(methodType: MethodType) {
        viewModelScope.launch(Dispatchers.IO){
            methodTypeRepository.insertMethodType(methodType)
        }
    }

    fun updateMethodType(methodType: MethodType) {
        viewModelScope.launch(Dispatchers.IO){
            methodTypeRepository.updateMethodType(methodType)
        }
    }

    fun deleteMethodType(methodType: MethodType) {
        viewModelScope.launch(Dispatchers.IO){
            methodTypeRepository.deleteMethodType(methodType)
        }
    }
}