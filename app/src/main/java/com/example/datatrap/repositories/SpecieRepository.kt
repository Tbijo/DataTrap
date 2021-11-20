package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.SpecieDao
import com.example.datatrap.models.Specie
import com.example.datatrap.models.tuples.SpecList
import com.example.datatrap.models.tuples.SpecSelectList

class SpecieRepository(private val specieDao: SpecieDao) {

    val specieList: LiveData<List<SpecList>> = specieDao.getSpecies()

    suspend fun insertSpecie(specie: Specie) {
        specieDao.insertSpecie(specie)
    }

    suspend fun updateSpecie(specie: Specie) {
        specieDao.updateSpecie(specie)
    }

    suspend fun deleteSpecie(specieId: Long) {
        specieDao.deleteSpecie(specieId)
    }

    fun getSpecie(specieId: Long): LiveData<Specie> {
        return specieDao.getSpecie(specieId)
    }

    fun getSpeciesForSelect(): LiveData<List<SpecSelectList>> {
        return specieDao.getSpeciesForSelect()
    }

    fun searchSpecies(specieCode: String): LiveData<List<SpecList>> {
        return specieDao.searchSpecies(specieCode)
    }

}