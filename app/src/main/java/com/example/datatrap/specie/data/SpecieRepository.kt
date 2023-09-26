package com.example.datatrap.specie.data

import com.example.datatrap.specie.domain.model.SpecList
import com.example.datatrap.specie.domain.model.SpecSelectList

class SpecieRepository(private val specieDao: SpecieDao) {

    suspend fun getSpecies(): List<SpecieEntity> {
        return specieDao.getSpecies()
    }

    suspend fun insertSpecie(specieEntity: SpecieEntity): String {
        return specieDao.insertSpecie(specieEntity)
    }

    suspend fun deleteSpecie(specieEntity: SpecieEntity) {
        specieDao.deleteSpecie(specieEntity)
    }

    suspend fun getSpecie(specieId: String): SpecieEntity {
        return specieDao.getSpecie(specieId)
    }

    suspend fun getSpeciesForSelect(): List<SpecSelectList> {
        return specieDao.getSpeciesForSelect()
    }

    suspend fun searchSpecies(specieCode: String): List<SpecieEntity> {
        return specieDao.searchSpecies(specieCode)
    }

    suspend fun getNonSpecie(spCode: List<String>): List<SpecList> {
        return specieDao.getNonSpecie(spCode)
    }

}