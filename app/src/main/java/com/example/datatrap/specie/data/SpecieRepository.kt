package com.example.datatrap.specie.data

import androidx.lifecycle.LiveData
import com.example.datatrap.specie.domain.model.SpecList
import com.example.datatrap.specie.domain.model.SpecSelectList

class SpecieRepository(private val specieDao: SpecieDao) {

    val specieList: LiveData<List<SpecList>> = specieDao.getSpecies()

    suspend fun insertSpecie(specieEntity: SpecieEntity): Long {
        return specieDao.insertSpecie(specieEntity)
    }

    suspend fun updateSpecie(specieEntity: SpecieEntity) {
        specieDao.updateSpecie(specieEntity)
    }

    suspend fun deleteSpecie(specieId: Long) {
        specieDao.deleteSpecie(specieId)
    }

    fun getSpecie(specieId: Long): LiveData<SpecieEntity> {
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