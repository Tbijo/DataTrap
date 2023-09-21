package com.example.datatrap.locality.data.locality

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocality(localityEntity: LocalityEntity)

    @Delete
    suspend fun deleteLocality(localityEntity: LocalityEntity)

    @Query("SELECT * FROM LocalityEntity WHERE localityId = :localityId")
    suspend fun getLocality(localityId: String): LocalityEntity

    @Query("SELECT * FROM LocalityEntity")
    fun getLocalities(): Flow<List<LocalityEntity>>

    @Query("SELECT * FROM LocalityEntity WHERE localityName LIKE :localityName")
    fun searchLocalities(localityName: String): Flow<List<LocalityEntity>>

    // SYNC
    @Query("SELECT * FROM LocalityEntity WHERE localityId IN (:localityIds)")
    suspend fun getLocalityForSync(localityIds: List<String>): List<LocalityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncLocality(localityEntity: LocalityEntity): String

    @Query("SELECT * FROM LocalityEntity WHERE localityName = :localityName")
    suspend fun getLocalityByName(localityName: String): LocalityEntity?

}