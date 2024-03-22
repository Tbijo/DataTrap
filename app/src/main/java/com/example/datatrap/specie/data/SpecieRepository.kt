package com.example.datatrap.specie.data

import com.example.datatrap.core.util.EnumSpecie
import com.example.datatrap.specie.domain.model.SpecList
import kotlinx.coroutines.flow.Flow

class SpecieRepository(private val specieDao: SpecieDao) {

    fun getSpecies(): Flow<List<SpecieEntity>> {
        return specieDao.getSpecies()
    }

    suspend fun insertSpecie(specieEntity: SpecieEntity) {
        specieDao.insertSpecie(specieEntity)
    }

    suspend fun deleteSpecie(specieEntity: SpecieEntity) {
        specieDao.deleteSpecie(specieEntity)
    }

    suspend fun getSpecie(specieId: String): SpecieEntity {
        return specieDao.getSpecie(specieId)
    }

    fun searchSpecies(specieCode: String): Flow<List<SpecieEntity>> {
        val searchQuery = "%$specieCode%"
        return specieDao.searchSpecies(searchQuery)
    }

    suspend fun getNonSpecie(): List<SpecList> {
        val specieCodeStr = EnumSpecie.values().map { it.name }

        return specieDao.getNonSpecie(specieCodeStr)
    }

}