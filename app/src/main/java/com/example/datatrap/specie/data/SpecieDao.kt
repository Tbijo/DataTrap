package com.example.datatrap.specie.data

import androidx.room.*
import com.example.datatrap.specie.domain.model.SpecList
import com.example.datatrap.specie.domain.model.SpecSelectList
import kotlinx.coroutines.flow.Flow

@Dao
interface SpecieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecie(specieEntity: SpecieEntity): Long

    @Query("DELETE FROM SpecieEntity WHERE specieId = :specieId")
    suspend fun deleteSpecie(specieId: Long)

    @Query("SELECT * FROM SpecieEntity WHERE specieId = :specieId")
    fun getSpecie(specieId: Long): Flow<SpecieEntity>

    @Query("SELECT specieId, speciesCode, fullName FROM SpecieEntity")
    fun getSpecies(): Flow<List<SpecList>>

    @Query("SELECT specieId, speciesCode, upperFingers, minWeight, maxWeight FROM SpecieEntity")
    fun getSpeciesForSelect(): Flow<List<SpecSelectList>>

    @Query("SELECT specieId, speciesCode, fullName FROM SpecieEntity WHERE speciesCode LIKE :specieCode")
    fun searchSpecies(specieCode: String): Flow<List<SpecList>>

    @Query("SELECT specieId, speciesCode, fullName FROM SpecieEntity WHERE speciesCode IN (:spCode)")
    fun getNonSpecie(spCode: List<String>): Flow<List<SpecList>>
}