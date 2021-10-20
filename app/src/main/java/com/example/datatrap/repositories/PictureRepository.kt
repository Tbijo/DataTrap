package com.example.datatrap.repositories

import com.example.datatrap.databaseio.dao.PictureDao
import com.example.datatrap.models.Picture

class PictureRepository(private val pictureDao: PictureDao) {

    suspend fun insertPicture(picture: Picture) {
        pictureDao.insertPicture(picture)
    }

    suspend fun deletePicture(picture: Picture) {
        pictureDao.deletePicture(picture)
    }

    suspend fun getPictureById(pictureName: String): Picture? {
        return pictureDao.getPictureById(pictureName)
    }

}