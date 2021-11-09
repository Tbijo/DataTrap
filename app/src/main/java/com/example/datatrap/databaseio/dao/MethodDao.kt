package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.Method

@Dao
interface MethodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMethod(method: Method)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun initInsert(method: Method)

    @Update
    suspend fun updateMethod(method: Method)

    @Delete
    suspend fun deleteMethod(method: Method)

    @Query("SELECT * FROM Method")
    fun getMethods(): LiveData<List<Method>>
}