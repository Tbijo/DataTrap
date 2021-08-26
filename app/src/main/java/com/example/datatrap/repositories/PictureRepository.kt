package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.PictureDao
import com.example.datatrap.models.Picture

class PictureRepository(private val pictureDao: PictureDao) {

    suspend fun insertPicture(picture: Picture) {
        pictureDao.insertPicture(picture)
    }

    suspend fun deletePicture(picture: Picture) {
        pictureDao.deletePicture(picture)
    }

    fun getPictureById(pictureID: String): LiveData<Picture>{
        return pictureDao.getPictureById(pictureID)
    }

}