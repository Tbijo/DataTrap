package com.example.datatrap.settings.traptype.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TrapTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrapType(trapType: TrapType)

    @Update
    suspend fun updateTrapType(trapType: TrapType)

    @Delete
    suspend fun deleteTrapType(trapType: TrapType)

    @Query("SELECT * FROM TrapType")
    fun getTrapTypes(): LiveData<List<TrapType>>
}