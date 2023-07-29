package com.example.datatrap.settings.vegettype.data

import androidx.lifecycle.LiveData
import androidx.room.*

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