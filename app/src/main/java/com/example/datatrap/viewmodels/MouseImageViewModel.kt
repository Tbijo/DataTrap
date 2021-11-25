package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.MouseImage
import com.example.datatrap.repositories.MouseImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MouseImageViewModel(application: Application) : AndroidViewModel(application) {

    private val mouseImageRepository: MouseImageRepository

    init {
        val mouseImageDao = TrapDatabase.getDatabase(application).mouseImageDao()
        mouseImageRepository = MouseImageRepository(mouseImageDao)
    }

    fun insertImage(mouseImage: MouseImage) {
        viewModelScope.launch(Dispatchers.IO) {
            mouseImageRepository.insertImage(mouseImage)
        }
    }

    fun deleteImage(mouseImgId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            mouseImageRepository.deleteImage(mouseImgId)
        }
    }

    fun getImageForMouse(mouseId: Long): LiveData<MouseImage> {
        return mouseImageRepository.getImageForMouse(mouseId)
    }
}