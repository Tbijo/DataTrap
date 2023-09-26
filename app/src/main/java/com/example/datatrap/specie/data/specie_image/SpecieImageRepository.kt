package com.example.datatrap.specie.data.specie_image

import java.io.File

class SpecieImageRepository(private val specieImageDao: SpecieImageDao) {

    suspend fun insertImage(specieImageEntity: SpecieImageEntity) {
        specieImageDao.insertImage(specieImageEntity)
    }

    suspend fun deleteImage(specieImageEntity: SpecieImageEntity) {
        val myFile = File(specieImageEntity.path)
        if (myFile.exists()) myFile.delete()
        specieImageDao.deleteImage(specieImageEntity)
    }

    suspend fun getImageForSpecie(specieId: String): SpecieImageEntity? {
        return specieImageDao.getImageForSpecie(specieId)
    }
}