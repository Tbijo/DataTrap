package com.example.datatrap.locality.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.locality.data.Locality
import com.example.datatrap.locality.data.LocList

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

    // SYNC
    @Query("SELECT * FROM Locality WHERE localityId IN (:localityIds)")
    suspend fun getLocalityForSync(localityIds: List<Long>): List<Locality>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncLocality(locality: Locality): Long

    @Query("SELECT * FROM locality WHERE localityName = :localityName")
    suspend fun getLocalityByName(localityName: String): Locality?

}