package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.SpecieImage
import com.example.datatrap.repositories.SpecieImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SpecieImageViewModel(application: Application) : AndroidViewModel(application) {

    private val specieImageRepository: SpecieImageRepository

    init {
        val specieImageDao = TrapDatabase.getDatabase(application).specieImageDao()
        specieImageRepository = SpecieImageRepository(specieImageDao)
    }

    fun insertImage(specieImage: SpecieImage) {
        viewModelScope.launch(Dispatchers.IO) {
            specieImageRepository.insertImage(specieImage)
        }
    }

    fun deleteImage(specieImgId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            specieImageRepository.deleteImage(specieImgId)
        }
    }

    fun getImageForSpecie(specieId: Long): LiveData<SpecieImage> {
        return specieImageRepository.getImageForSpecie(specieId)
    }
}