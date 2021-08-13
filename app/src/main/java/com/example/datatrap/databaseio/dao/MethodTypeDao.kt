package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.MethodType
import kotlinx.coroutines.flow.Flow

@Dao
interface MethodTypeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMethodType(methodType: MethodType)

    @Update
    suspend fun updateMethodType(methodType: MethodType)

    @Delete
    suspend fun deleteMethodType(methodType: MethodType)

    @Query("SELECT * FROM method_types WHERE MethodTypeName = :methodTypeName")
    suspend fun getMethodType(methodTypeName: String): LiveData<MethodType>

    @Query("SELECT * FROM method_types")
    fun getMethodTypes(): LiveData<List<MethodType>>
}