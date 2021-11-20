package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.Locality
import com.example.datatrap.models.tuples.LocList

@Dao
interface LocalityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocality(locality: Locality)

    @Update
    suspend fun updateLocality(locality: Locality)

    @Query("DELETE FROM Locality WHERE localityId = :localityId")
    suspend fun deleteLocality(localityId: Long)

    @Query("SELECT * FROM Locality WHERE localityId = :localityId")
    fun getLocality(localityId: Long): LiveData<Locality>

    @Query("SELECT localityId, localityName, localityDateTimeCreated AS dateTime, xA, yA, numSessions FROM Locality")
    fun getLocalities(): LiveData<List<LocList>>

    @Query("SELECT localityId, localityName, localityDateTimeCreated AS dateTime, xA, yA, numSessions FROM Locality WHERE localityName LIKE :localityName")
    fun searchLocalities(localityName: String): LiveData<List<LocList>>

}