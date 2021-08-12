package com.example.datatrap.repositories

import com.example.datatrap.databaseio.dao.PictureDao
import com.example.datatrap.models.Picture
import kotlinx.coroutines.flow.Flow

class PictureRepository(private val pictureDao: PictureDao) {

    suspend fun insertPicture(picture: Picture) {
        pictureDao.insertPicture(picture)
    }

    suspend fun deletePicture(picture: Picture) {
        pictureDao.deletePicture(picture)
    }

    fun getPicture(pictureID: String): Flow<List<Picture>>{
        return pictureDao.getPicture(pictureID)
    }

}