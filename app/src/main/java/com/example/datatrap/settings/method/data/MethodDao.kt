package com.example.datatrap.settings.method.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MethodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMethod(method: Method)

    @Update
    suspend fun updateMethod(method: Method)

    @Delete
    suspend fun deleteMethod(method: Method)

    @Query("SELECT * FROM Method")
    fun getMethods(): LiveData<List<Method>>
}