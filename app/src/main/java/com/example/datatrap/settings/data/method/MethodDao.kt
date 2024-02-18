package com.example.datatrap.settings.data.method

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MethodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMethod(methodEntity: MethodEntity)

    @Delete
    suspend fun deleteMethod(methodEntity: MethodEntity)

    @Query("SELECT * FROM MethodEntity WHERE methodId = :methodId")
    fun getMethod(methodId: String): Flow<MethodEntity>

    @Query("SELECT * FROM MethodEntity")
    fun getMethods(): Flow<List<MethodEntity>>
}