package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.Picture
import com.example.datatrap.repositories.PictureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PictureViewModel(application: Application) : AndroidViewModel(application) {

    private val pictureRepository: PictureRepository

    init {
        val pictureDao = TrapDatabase.getDatabase(application).pictureDao()
        pictureRepository = PictureRepository(pictureDao)
    }

    fun insertPicture(picture: Picture) {
        viewModelScope.launch(Dispatchers.IO) {
            pictureRepository.insertPicture(picture)
        }
    }

    fun deletePicture(picture: Picture) {
        viewModelScope.launch(Dispatchers.IO) {
            pictureRepository.deletePicture(picture)
        }
    }

    fun getPictureById(pictureName: String): Picture {
        val picture: Picture
        runBlocking {
            picture = pictureRepository.getPictureById(pictureName)
        }
        return picture
    }
}