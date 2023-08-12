package com.example.datatrap.settings.envtype.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EnvTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnvType(envTypeEntity: EnvTypeEntity)

    @Update
    suspend fun updateEnvType(envTypeEntity: EnvTypeEntity)

    @Delete
    suspend fun deleteEnvType(envTypeEntity: EnvTypeEntity)

    @Query("SELECT * FROM EnvTypeEntity")
    fun getEnvTypes(): LiveData<List<EnvTypeEntity>>
}