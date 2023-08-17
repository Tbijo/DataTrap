package com.example.datatrap.settings.methodtype.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MethodTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMethodType(methodTypeEntity: MethodTypeEntity)

    @Delete
    suspend fun deleteMethodType(methodTypeEntity: MethodTypeEntity)

    @Query("SELECT * FROM MethodTypeEntity")
    fun getMethodTypes(): Flow<List<MethodTypeEntity>>
}