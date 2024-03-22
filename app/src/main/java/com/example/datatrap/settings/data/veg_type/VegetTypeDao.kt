package com.example.datatrap.settings.data.veg_type

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VegetTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVegetType(vegetTypeEntity: VegetTypeEntity)

    @Delete
    suspend fun deleteVegetType(vegetTypeEntity: VegetTypeEntity)

    @Query("SELECT * FROM VegetTypeEntity WHERE vegetTypeId = :vegetTypeId")
    suspend fun getVegetType(vegetTypeId: String): VegetTypeEntity

    @Query("SELECT * FROM VegetTypeEntity")
    fun getVegetTypes(): Flow<List<VegetTypeEntity>>
}