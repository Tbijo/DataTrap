package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.SpecieImageDao
import com.example.datatrap.models.SpecieImage

class SpecieImageRepository(private val specieImageDao: SpecieImageDao) {

    suspend fun insertImage(specieImage: SpecieImage) {
        specieImageDao.insertImage(specieImage)
    }

    suspend fun deleteImage(specieImgId: Long) {
        specieImageDao.deleteImage(specieImgId)
    }

    fun getImageForSpecie(specieId: Long): LiveData<SpecieImage> {
        return specieImageDao.getImageForSpecie(specieId)
    }
}