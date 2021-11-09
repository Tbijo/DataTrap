package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.EnvType

@Dao
interface EnvTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnvType(envType: EnvType)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun initInsert(envType: EnvType)

    @Update
    suspend fun updateEnvType(envType: EnvType)

    @Delete
    suspend fun deleteEnvType(envType: EnvType)

    @Query("SELECT * FROM EnvType")
    fun getEnvTypes(): LiveData<List<EnvType>>
}