package com.example.datatrap.locality.data

import androidx.room.*
import com.example.datatrap.locality.domain.model.LocList
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocality(localityEntity: LocalityEntity)

    @Query("DELETE FROM LocalityEntity WHERE localityId = :localityId")
    suspend fun deleteLocality(localityId: Long)

    @Query("SELECT * FROM LocalityEntity WHERE localityId = :localityId")
    fun getLocality(localityId: String): Flow<LocalityEntity>

    @Query("SELECT localityId, localityName, localityDateTimeCreated AS dateTime, xA, yA, numSessions FROM LocalityEntity")
    fun getLocalities(): Flow<List<LocList>>

    @Query("SELECT localityId, localityName, localityDateTimeCreated AS dateTime, xA, yA, numSessions FROM LocalityEntity WHERE localityName LIKE :localityName")
    fun searchLocalities(localityName: String): Flow<List<LocList>>

    // SYNC
    @Query("SELECT * FROM LocalityEntity WHERE localityId IN (:localityIds)")
    suspend fun getLocalityForSync(localityIds: List<Long>): List<LocalityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncLocality(localityEntity: LocalityEntity): Long

    @Query("SELECT * FROM LocalityEntity WHERE localityName = :localityName")
    suspend fun getLocalityByName(localityName: String): LocalityEntity?

}