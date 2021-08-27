package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.EnvType

@Dao
interface EnvTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnvType(envType: EnvType)

    @Update
    suspend fun updateEnvType(envType: EnvType)

    @Delete
    suspend fun deleteEnvType(envType: EnvType)

    @Query("SELECT * FROM env_types WHERE EnvTypeName = :EnvTypeName")
    suspend fun getEnvType(EnvTypeName: String): LiveData<EnvType>

    @Query("SELECT * FROM env_types")
    fun getEnvTypes(): LiveData<List<EnvType>>
}