package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.TrapType
import kotlinx.coroutines.flow.Flow

@Dao
interface TrapTypeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrapType(trapType: TrapType)

    @Update
    suspend fun updateTrapType(trapType: TrapType)

    @Delete
    suspend fun deleteTrapType(trapType: TrapType)

    @Query("SELECT * FROM trap_types WHERE TrapTypeName = :trapTypeName")
    suspend fun getTrapType(trapTypeName: String): LiveData<TrapType>

    @Query("SELECT * FROM trap_types")
    fun getTrapTypes(): LiveData<List<TrapType>>
}