package com.example.datatrap.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.models.SpecieImage
import com.example.datatrap.models.sync.SpecieImageSync
import com.example.datatrap.repositories.SpecieImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SpecieImageViewModel @Inject constructor(
    private val specieImageRepository: SpecieImageRepository
) : ViewModel() {

    fun insertImage(specieImage: SpecieImage) {
        viewModelScope.launch(Dispatchers.IO) {
            specieImageRepository.insertImage(specieImage)
        }
    }

    fun deleteImage(specieImgId: Long, imagePath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val job1 = launch {
                // vymazat subor starej fotky
                val myFile = File(imagePath)
                if (myFile.exists()) myFile.delete()
            }
            val job2 = launch {
                specieImageRepository.deleteImage(specieImgId)
            }
            job1.join()
            job2.join()
        }
    }

    fun getImageForSpecie(specieId: Long): LiveData<SpecieImage> {
        return specieImageRepository.getImageForSpecie(specieId)
    }
}