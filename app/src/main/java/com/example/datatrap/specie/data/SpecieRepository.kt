package com.example.datatrap.specie.data

import androidx.lifecycle.LiveData

class SpecieRepository(private val specieDao: SpecieDao) {

    val specieList: LiveData<List<SpecList>> = specieDao.getSpecies()

    suspend fun insertSpecie(specie: Specie): Long {
        return specieDao.insertSpecie(specie)
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

    fun getNonSpecie(spCode: List<String>): LiveData<List<SpecList>> {
        return specieDao.getNonSpecie(spCode)
    }

}