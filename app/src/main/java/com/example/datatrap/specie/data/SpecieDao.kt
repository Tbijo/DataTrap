package com.example.datatrap.specie.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.datatrap.specie.domain.model.SpecList
import kotlinx.coroutines.flow.Flow

@Dao
interface SpecieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecie(specieEntity: SpecieEntity)

    @Delete
    suspend fun deleteSpecie(specieEntity: SpecieEntity)

    @Query("SELECT * FROM SpecieEntity WHERE specieId = :specieId")
    suspend fun getSpecie(specieId: String): SpecieEntity

    @Query("SELECT * FROM SpecieEntity")
    fun getSpecies(): Flow<List<SpecieEntity>>

    @Query("SELECT * FROM SpecieEntity WHERE speciesCode LIKE :specieCode")
    fun searchSpecies(specieCode: String): Flow<List<SpecieEntity>>

    @Query("SELECT specieId, speciesCode, fullName FROM SpecieEntity WHERE speciesCode IN (:spCode)")
    suspend fun getNonSpecie(spCode: List<String>): List<SpecList>
}