package com.example.datatrap.specie.data

import androidx.room.*
import com.example.datatrap.specie.domain.model.SpecList
import com.example.datatrap.specie.domain.model.SpecSelectList

@Dao
interface SpecieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecie(specieEntity: SpecieEntity)

    @Delete
    suspend fun deleteSpecie(specieEntity: SpecieEntity)

    @Query("SELECT * FROM SpecieEntity WHERE specieId = :specieId")
    suspend fun getSpecie(specieId: String): SpecieEntity

    @Query("SELECT * FROM SpecieEntity")
    suspend fun getSpecies(): List<SpecieEntity>

    @Query("SELECT specieId, speciesCode, upperFingers, minWeight, maxWeight FROM SpecieEntity")
    suspend fun getSpeciesForSelect(): List<SpecSelectList>

    @Query("SELECT * FROM SpecieEntity WHERE speciesCode LIKE :specieCode")
    suspend fun searchSpecies(specieCode: String): List<SpecieEntity>

    @Query("SELECT specieId, speciesCode, fullName FROM SpecieEntity WHERE speciesCode IN (:spCode)")
    suspend fun getNonSpecie(spCode: List<String>): List<SpecList>
}