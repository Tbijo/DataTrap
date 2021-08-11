package com.example.datatrap.databaseio.dao

import androidx.room.*
import com.example.datatrap.models.Method
import kotlinx.coroutines.flow.Flow

@Dao
interface MethodDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMethod(method: Method)

    @Update
    suspend fun updateMethod(method: Method)

    @Delete
    suspend fun deleteMethod(method: Method)

    @Query("SELECT * FROM methods WHERE MethodName = :methodName")
    suspend fun getMethod(methodName: String): Method

    @Query("SELECT * FROM methods")
    fun getMethods(): Flow<List<Method>>
}