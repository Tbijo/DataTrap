package com.example.datatrap.settings.methodtype.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MethodTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMethodType(methodType: MethodType)

    @Update
    suspend fun updateMethodType(methodType: MethodType)

    @Delete
    suspend fun deleteMethodType(methodType: MethodType)

    @Query("SELECT * FROM MethodType")
    fun getMethodTypes(): LiveData<List<MethodType>>
}