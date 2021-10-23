package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.Specie

@Dao
interface SpecieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecie(specie: Specie)

    @Update
    suspend fun updateSpecie(specie: Specie)

    @Delete
    suspend fun deleteSpecie(specie: Specie)

    @Query("SELECT * FROM Specie WHERE specieId = :specieId")
    suspend fun getSpecie(specieId: Long): Specie?

    // pouzije sa pri fragmente na vyber pri pridavani mouse
        // ale vyberu sa len ich nazvy vo view
    @Query("SELECT * FROM Specie")
    fun getSpecies(): LiveData<List<Specie>>

    @Query("SELECT * FROM Specie WHERE speciesCode LIKE :specieCode")
    fun searchSpecies(specieCode: String): LiveData<List<Specie>>
}