package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.OccasionImage
import com.example.datatrap.repositories.OccasionImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OccasionImageViewModel(application: Application) : AndroidViewModel(application) {

    private val occasionImageRepository: OccasionImageRepository

    init {
        val occasionImageDao = TrapDatabase.getDatabase(application).occasionImageDao()
        occasionImageRepository = OccasionImageRepository(occasionImageDao)
    }

    fun insertImage(occasionImage: OccasionImage) {
        viewModelScope.launch(Dispatchers.IO) {
            occasionImageRepository.insertImage(occasionImage)
        }
    }

    fun deleteImage(occasionImgId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            occasionImageRepository.deleteImage(occasionImgId)
        }
    }

    fun getImageForOccasion(occasionId: Long): LiveData<OccasionImage> {
        return occasionImageRepository.getImageForOccasion(occasionId)
    }
}