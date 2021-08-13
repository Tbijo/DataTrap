package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.Locality
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocality(locality: Locality)

    @Update
    suspend fun updateLocality(locality: Locality)

    @Delete
    suspend fun deleteLocality(locality: Locality)

    @Query("SELECT * FROM locality WHERE LocalityName = :localityName")
    suspend fun getLocality(localityName: String): LiveData<Locality>

    @Query("SELECT * FROM locality")
    fun getLocalities(): LiveData<List<Locality>>

    // pridat getLocalities podla zvoleneho id Projektu

    @Query("SELECT * FROM locality WHERE LocalityName LIKE :localityName")
    fun searchLocalities(localityName: String): LiveData<List<Locality>>

    // mozno pridat metodu na ziskanie najnovsej lokality
}