package com.example.datatrap.settings.envtype.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EnvTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnvType(envType: EnvType)

    @Update
    suspend fun updateEnvType(envType: EnvType)

    @Delete
    suspend fun deleteEnvType(envType: EnvType)

    @Query("SELECT * FROM EnvType")
    fun getEnvTypes(): LiveData<List<EnvType>>
}