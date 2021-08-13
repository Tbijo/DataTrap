package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.Specie
import kotlinx.coroutines.flow.Flow

@Dao
interface SpecieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSpecie(specie: Specie)

    @Update
    suspend fun updateSpecie(specie: Specie)

    @Delete
    suspend fun deleteSpecie(specie: Specie)

    @Query("SELECT * FROM species WHERE Species_code = :speciecode")
    suspend fun getSpecie(speciecode: String): LiveData<Specie>

    // pouzije sa pri fragmente na vyber pri pridavani mouse
        // ale vyberu sa len ich nazvy vo view
    @Query("SELECT * FROM species")
    fun getSpecies(): LiveData<List<Specie>>

    @Query("SELECT * FROM species WHERE Species_code LIKE :specieCode")
    fun searchSpecies(specieCode: String): LiveData<List<Specie>>
}