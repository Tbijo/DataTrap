package com.example.datatrap.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.models.MouseImage
import com.example.datatrap.models.sync.MouseImageSync
import com.example.datatrap.repositories.MouseImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MouseImageViewModel @Inject constructor(
    private val mouseImageRepository: MouseImageRepository
) : ViewModel() {

    fun insertImage(mouseImage: MouseImage) {
        viewModelScope.launch(Dispatchers.IO) {
            mouseImageRepository.insertImage(mouseImage)
        }
    }

    fun deleteImage(mouseImgId: Long, filePath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val job1 = launch {
                val myFile = File(filePath)
                if (myFile.exists()) myFile.delete()
            }
            val job2 = launch {
                mouseImageRepository.deleteImage(mouseImgId)
            }
            job1.join()
            job2.join()
        }
    }

    fun getImageForMouse(mouseIid: Long, deviceID: String): LiveData<MouseImage> {
        return mouseImageRepository.getImageForMouse(mouseIid, deviceID)
    }
}