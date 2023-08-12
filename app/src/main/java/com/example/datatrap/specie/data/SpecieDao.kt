package com.example.datatrap.specie.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.specie.domain.model.SpecList
import com.example.datatrap.specie.domain.model.SpecSelectList

@Dao
interface SpecieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecie(specieEntity: SpecieEntity): Long

    @Update
    suspend fun updateSpecie(specieEntity: SpecieEntity)

    @Query("DELETE FROM SpecieEntity WHERE specieId = :specieId")
    suspend fun deleteSpecie(specieId: Long)

    @Query("SELECT * FROM SpecieEntity WHERE specieId = :specieId")
    fun getSpecie(specieId: Long): LiveData<SpecieEntity>

    @Query("SELECT specieId, speciesCode, fullName FROM SpecieEntity")
    fun getSpecies(): LiveData<List<SpecList>>

    @Query("SELECT specieId, speciesCode, upperFingers, minWeight, maxWeight FROM SpecieEntity")
    fun getSpeciesForSelect(): LiveData<List<SpecSelectList>>

    @Query("SELECT specieId, speciesCode, fullName FROM SpecieEntity WHERE speciesCode LIKE :specieCode")
    fun searchSpecies(specieCode: String): LiveData<List<SpecList>>

    @Query("SELECT specieId, speciesCode, fullName FROM SpecieEntity WHERE speciesCode IN (:spCode)")
    fun getNonSpecie(spCode: List<String>): LiveData<List<SpecList>>
}