package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.Specie
import com.example.datatrap.models.tuples.SpecList
import com.example.datatrap.models.tuples.SpecSelectList

@Dao
interface SpecieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecie(specie: Specie)

    @Update
    suspend fun updateSpecie(specie: Specie)

    @Query("DELETE FROM Specie WHERE specieId = :specieId")
    suspend fun deleteSpecie(specieId: Long)

    @Query("SELECT * FROM Specie WHERE specieId = :specieId")
    fun getSpecie(specieId: Long): LiveData<Specie>

    @Query("SELECT specieId, speciesCode, fullName, imgName FROM Specie")
    fun getSpecies(): LiveData<List<SpecList>>

    @Query("SELECT specieId, speciesCode, upperFingers, minWeight, maxWeight FROM Specie")
    fun getSpeciesForSelect(): LiveData<List<SpecSelectList>>

    @Query("SELECT specieId, speciesCode, fullName FROM Specie WHERE speciesCode LIKE :specieCode")
    fun searchSpecies(specieCode: String): LiveData<List<SpecList>>
}