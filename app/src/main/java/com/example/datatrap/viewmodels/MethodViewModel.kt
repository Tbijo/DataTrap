package com.example.datatrap.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.models.Method
import com.example.datatrap.repositories.MethodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MethodViewModel @Inject constructor(
    private val methodRepository: MethodRepository
): ViewModel() {

    val methodList: LiveData<List<Method>> = methodRepository.methodList

    fun insertMethod(method: Method){
        viewModelScope.launch(Dispatchers.IO) {
            methodRepository.insertMethod(method)
        }
    }

    fun updateMethod(method: Method){
        viewModelScope.launch(Dispatchers.IO) {
            methodRepository.updateMethod(method)
        }
    }

    fun deleteMethod(method: Method){
        viewModelScope.launch(Dispatchers.IO) {
            methodRepository.deleteMethod(method)
        }
    }
}