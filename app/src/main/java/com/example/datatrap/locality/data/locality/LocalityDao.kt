package com.example.datatrap.locality.data.locality

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

}