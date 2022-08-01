package com.example.datatrap.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.models.OccasionImage
import com.example.datatrap.models.sync.OccasionImageSync
import com.example.datatrap.repositories.OccasionImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class OccasionImageViewModel @Inject constructor(
    private val occasionImageRepository: OccasionImageRepository
) : ViewModel() {

    fun insertImage(occasionImage: OccasionImage) {
        viewModelScope.launch(Dispatchers.IO) {
            occasionImageRepository.insertImage(occasionImage)
        }
    }

    fun deleteImage(occasionImgId: Long, imagePath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val job1 = launch {
                val myFile = File(imagePath)
                if (myFile.exists()) myFile.delete()
            }
            val job2 = launch {
                occasionImageRepository.deleteImage(occasionImgId)
            }
            job1.join()
            job2.join()
        }
    }

    fun getImageForOccasion(occasionId: Long): LiveData<OccasionImage> {
        return occasionImageRepository.getImageForOccasion(occasionId)
    }
}