package com.example.datatrap.specie.data

import com.example.datatrap.specie.domain.model.SpecList
import com.example.datatrap.specie.domain.model.SpecSelectList
import kotlinx.coroutines.flow.Flow

class SpecieRepository(private val specieDao: SpecieDao) {

    val specieList: Flow<List<SpecList>> = specieDao.getSpecies()

    suspend fun insertSpecie(specieEntity: SpecieEntity): Long {
        return specieDao.insertSpecie(specieEntity)
    }

    suspend fun deleteSpecie(specieId: Long) {
        specieDao.deleteSpecie(specieId)
    }

    fun getSpecie(specieId: Long): Flow<SpecieEntity> {
        return specieDao.getSpecie(specieId)
    }

    fun getSpeciesForSelect(): Flow<List<SpecSelectList>> {
        return specieDao.getSpeciesForSelect()
    }

    fun searchSpecies(specieCode: String): Flow<List<SpecList>> {
        return specieDao.searchSpecies(specieCode)
    }

    fun getNonSpecie(spCode: List<String>): Flow<List<SpecList>> {
        return specieDao.getNonSpecie(spCode)
    }

}