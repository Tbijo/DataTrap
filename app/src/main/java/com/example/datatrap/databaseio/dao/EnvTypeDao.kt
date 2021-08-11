package com.example.datatrap.databaseio.dao

import androidx.room.*
import com.example.datatrap.models.EnvType
import kotlinx.coroutines.flow.Flow

@Dao
interface EnvTypeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEnvType(envType: EnvType)

    @Update
    suspend fun updateEnvType(envType: EnvType)

    @Delete
    suspend fun deleteEnvType(envType: EnvType)

    @Query("SELECT * FROM env_types WHERE EnvTypeName = :EnvTypeName")
    suspend fun getEnvType(EnvTypeName: String): EnvType

    @Query("SELECT * FROM env_types")
    fun getEnvTypes(): Flow<List<EnvType>>
}