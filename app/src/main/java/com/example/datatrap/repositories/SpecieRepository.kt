package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.SpecieDao
import com.example.datatrap.models.Specie

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

    suspend fun getSpecie(specieId: Long): Specie? {
        return specieDao.getSpecie(specieId)
    }

    val specieList: LiveData<List<Specie>> = specieDao.getSpecies()

    fun searchSpecies(specieCode: String): LiveData<List<Specie>> {
        return specieDao.searchSpecies(specieCode)
    }

}