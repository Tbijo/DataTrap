package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.Method

@Dao
interface MethodDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMethod(method: Method)

    @Update
    suspend fun updateMethod(method: Method)

    @Delete
    suspend fun deleteMethod(method: Method)

    @Query("SELECT * FROM methods WHERE MethodName = :methodName")
    suspend fun getMethod(methodName: String): LiveData<Method>

    @Query("SELECT * FROM methods")
    fun getMethods(): LiveData<List<Method>>
}