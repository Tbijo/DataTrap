package com.example.datatrap.settings.data.methodtype

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MethodTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMethodType(methodTypeEntity: MethodTypeEntity)

    @Delete
    suspend fun deleteMethodType(methodTypeEntity: MethodTypeEntity)

    @Query("SELECT * FROM MethodTypeEntity WHERE methodTypeId = :methodTypeId")
    suspend fun getMethodType(methodTypeId: String): MethodTypeEntity

    @Query("SELECT * FROM MethodTypeEntity")
    fun getMethodTypes(): Flow<List<MethodTypeEntity>>
}