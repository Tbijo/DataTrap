package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.Locality

@Dao
interface LocalityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocality(locality: Locality)

    @Update
    suspend fun updateLocality(locality: Locality)

    @Delete
    suspend fun deleteLocality(locality: Locality)

    @Query("SELECT * FROM Locality WHERE localityId = :localityId")
    suspend fun getLocality(localityId: Long): Locality

    @Query("SELECT * FROM Locality")
    fun getLocalities(): LiveData<List<Locality>>

    @Query("SELECT * FROM Locality WHERE LocalityName LIKE :localityName")
    fun searchLocalities(localityName: String): LiveData<List<Locality>>

}