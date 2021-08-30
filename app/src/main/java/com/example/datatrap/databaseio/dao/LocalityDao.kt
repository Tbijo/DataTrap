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

    @Query("SELECT * FROM localities WHERE localityId = :localityId")
    fun getLocality(localityId: Long): LiveData<Locality>

    @Query("SELECT * FROM localities")
    fun getLocalities(): LiveData<List<Locality>>

    @Query("SELECT * FROM localities WHERE LocalityName LIKE :localityName")
    fun searchLocalities(localityName: String): LiveData<List<Locality>>

}