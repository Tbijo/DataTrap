package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.Method
import com.example.datatrap.repositories.MethodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MethodViewModel(application: Application): AndroidViewModel(application) {

    val methodList: Flow<List<Method>>
    private val methodRepository: MethodRepository

    init {
        val methodDao = TrapDatabase.getDatabase(application).methodDao()
        methodRepository = MethodRepository(methodDao)
        methodList = methodRepository.methodList
    }

    fun insertMethod(method: Method){
        viewModelScope.launch(Dispatchers.IO){
            methodRepository.insertMethod(method)
        }
    }

    fun updateMethod(method: Method){
        viewModelScope.launch(Dispatchers.IO){
            methodRepository.updateMethod(method)
        }
    }

    fun deleteMethod(method: Method){
        viewModelScope.launch(Dispatchers.IO){
            methodRepository.deleteMethod(method)
        }
    }
}