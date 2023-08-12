package com.example.datatrap.locality.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.locality.domain.model.LocList

@Dao
interface LocalityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocality(localityEntity: LocalityEntity)

    @Update
    suspend fun updateLocality(localityEntity: LocalityEntity)

    @Query("DELETE FROM LocalityEntity WHERE localityId = :localityId")
    suspend fun deleteLocality(localityId: Long)

    @Query("SELECT * FROM LocalityEntity WHERE localityId = :localityId")
    fun getLocality(localityId: Long): LiveData<LocalityEntity>

    @Query("SELECT localityId, localityName, localityDateTimeCreated AS dateTime, xA, yA, numSessions FROM LocalityEntity")
    fun getLocalities(): LiveData<List<LocList>>

    @Query("SELECT localityId, localityName, localityDateTimeCreated AS dateTime, xA, yA, numSessions FROM LocalityEntity WHERE localityName LIKE :localityName")
    fun searchLocalities(localityName: String): LiveData<List<LocList>>

    // SYNC
    @Query("SELECT * FROM LocalityEntity WHERE localityId IN (:localityIds)")
    suspend fun getLocalityForSync(localityIds: List<Long>): List<LocalityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncLocality(localityEntity: LocalityEntity): Long

    @Query("SELECT * FROM locality WHERE localityName = :localityName")
    suspend fun getLocalityByName(localityName: String): LocalityEntity?

}