package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.VegetType

@Dao
interface VegetTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVegetType(vegetType: VegetType)

    @Update
    suspend fun updateVegetType(vegetType: VegetType)

    @Delete
    suspend fun deleteVegetType(vegetType: VegetType)

    @Query("SELECT * FROM VegetType")
    fun getVegetTypes(): LiveData<List<VegetType>>
}