package com.example.datatrap.settings.data.env_type

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EnvTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnvType(envTypeEntity: EnvTypeEntity)

    @Delete
    suspend fun deleteEnvType(envTypeEntity: EnvTypeEntity)

    @Query("SELECT * FROM EnvTypeEntity WHERE envTypeId = :envTypeId")
    suspend fun getEnvType(envTypeId: String): EnvTypeEntity

    @Query("SELECT * FROM EnvTypeEntity")
    fun getEnvTypes(): Flow<List<EnvTypeEntity>>
}