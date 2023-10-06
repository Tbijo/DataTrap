package com.example.datatrap.specie.data.specie_image

class SpecieImageRepository(private val specieImageDao: SpecieImageDao) {

    suspend fun insertImage(specieImageEntity: SpecieImageEntity) {
        specieImageDao.insertImage(specieImageEntity)
    }

    suspend fun deleteImage(specieImageEntity: SpecieImageEntity) {
        // no deleting of images in gallery
        specieImageDao.deleteImage(specieImageEntity)
    }

    suspend fun getImageForSpecie(specieId: String): SpecieImageEntity? {
        return specieImageDao.getImageForSpecie(specieId)
    }
}