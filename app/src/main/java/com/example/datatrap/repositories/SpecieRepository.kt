package com.example.datatrap.repositories

import com.example.datatrap.databaseio.dao.SpecieDao
import com.example.datatrap.models.Specie
import kotlinx.coroutines.flow.Flow

class SpecieRepository(private val specieDao: SpecieDao) {

    suspend fun insertSpecie(specie: Specie){
        specieDao.insertSpecie(specie)
    }

    suspend fun updateSpecie(specie: Specie){
        specieDao.updateSpecie(specie)
    }

    suspend fun deleteSpecie(specie: Specie){
        specieDao.deleteSpecie(specie)
    }

    val specieList: Flow<List<Specie>> = specieDao.getSpecies()

    fun searchSpecies(specieCode: String): Flow<List<Specie>>{
        return specieDao.searchSpecies(specieCode)
    }

}