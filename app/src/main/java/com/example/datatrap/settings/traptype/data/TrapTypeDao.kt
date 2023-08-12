package com.example.datatrap.settings.traptype.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TrapTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrapType(trapTypeEntity: TrapTypeEntity)

    @Update
    suspend fun updateTrapType(trapTypeEntity: TrapTypeEntity)

    @Delete
    suspend fun deleteTrapType(trapTypeEntity: TrapTypeEntity)

    @Query("SELECT * FROM TrapTypeEntity")
    fun getTrapTypes(): LiveData<List<TrapTypeEntity>>
}