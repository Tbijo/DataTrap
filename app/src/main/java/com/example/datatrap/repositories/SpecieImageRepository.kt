package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.SpecieImageDao
import com.example.datatrap.models.SpecieImage
import com.example.datatrap.models.sync.SpecieImageSync

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

    suspend fun getSpecieImages(unixTime: Long): List<SpecieImageSync> {
        return specieImageDao.getSpecieImages(unixTime)
    }
}