package com.example.datatrap.settings.envtype.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EnvTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnvType(envTypeEntity: EnvTypeEntity)

    @Delete
    suspend fun deleteEnvType(envTypeEntity: EnvTypeEntity)

    @Query("SELECT * FROM EnvTypeEntity")
    fun getEnvTypes(): Flow<List<EnvTypeEntity>>
}