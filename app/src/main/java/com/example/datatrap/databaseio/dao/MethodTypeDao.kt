package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.MethodType

@Dao
interface MethodTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMethodType(methodType: MethodType)

    @Update
    suspend fun updateMethodType(methodType: MethodType)

    @Delete
    suspend fun deleteMethodType(methodType: MethodType)

    @Query("SELECT * FROM method_types")
    fun getMethodTypes(): LiveData<List<MethodType>>
}