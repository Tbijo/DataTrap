package com.example.datatrap.settings.methodtype.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MethodTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMethodType(methodTypeEntity: MethodTypeEntity)

    @Update
    suspend fun updateMethodType(methodTypeEntity: MethodTypeEntity)

    @Delete
    suspend fun deleteMethodType(methodTypeEntity: MethodTypeEntity)

    @Query("SELECT * FROM MethodTypeEntity")
    fun getMethodTypes(): LiveData<List<MethodTypeEntity>>
}