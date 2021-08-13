package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.VegetType
import kotlinx.coroutines.flow.Flow

@Dao
interface VegetTypeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVegetType(vegetType: VegetType)

    @Update
    suspend fun updateVegetType(vegetType: VegetType)

    @Delete
    suspend fun deleteVegetType(vegetType: VegetType)

    @Query("SELECT * FROM veget_types WHERE VegetTypeName = :vegetTypeName")
    suspend fun getVegetType(vegetTypeName: String): LiveData<VegetType>

    @Query("SELECT * FROM veget_types")
    fun getVegetTypes(): LiveData<List<VegetType>>
}