package com.example.datatrap.settings.method.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.settings.method.data.Method
import com.example.datatrap.settings.method.data.MethodRepository
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