package com.example.datatrap.settings.vegettype.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface VegetTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVegetType(vegetTypeEntity: VegetTypeEntity)

    @Update
    suspend fun updateVegetType(vegetTypeEntity: VegetTypeEntity)

    @Delete
    suspend fun deleteVegetType(vegetTypeEntity: VegetTypeEntity)

    @Query("SELECT * FROM VegetTypeEntity")
    fun getVegetTypes(): LiveData<List<VegetTypeEntity>>
}