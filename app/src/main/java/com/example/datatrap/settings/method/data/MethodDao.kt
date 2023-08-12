package com.example.datatrap.settings.method.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MethodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMethod(methodEntity: MethodEntity)

    @Update
    suspend fun updateMethod(methodEntity: MethodEntity)

    @Delete
    suspend fun deleteMethod(methodEntity: MethodEntity)

    @Query("SELECT * FROM MethodEntity")
    fun getMethods(): LiveData<List<MethodEntity>>
}