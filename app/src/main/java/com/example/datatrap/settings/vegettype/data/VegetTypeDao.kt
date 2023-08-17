package com.example.datatrap.settings.vegettype.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VegetTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVegetType(vegetTypeEntity: VegetTypeEntity)

    @Delete
    suspend fun deleteVegetType(vegetTypeEntity: VegetTypeEntity)

    @Query("SELECT * FROM VegetTypeEntity")
    fun getVegetTypes(): Flow<List<VegetTypeEntity>>
}