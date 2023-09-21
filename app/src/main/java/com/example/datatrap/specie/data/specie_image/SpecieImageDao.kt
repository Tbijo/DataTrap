package com.example.datatrap.specie.data.specie_image

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.datatrap.sync.data.remote.SpecieImageSync
import kotlinx.coroutines.flow.Flow

@Dao
interface SpecieImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(specieImageEntity: SpecieImageEntity)

    @Delete
    suspend fun deleteImage(specieImageEntity: SpecieImageEntity)

    @Query("SELECT * FROM SpecieImageEntity WHERE specieID = :specieId")
    fun getImageForSpecie(specieId: Long): Flow<SpecieImageEntity>

    @Query("SELECT imgName, path, note, specieID, uniqueCode FROM SpecieImageEntity WHERE uniqueCode >= :unixTime")
    suspend fun getSpecieImages(unixTime: Long): List<SpecieImageSync>
}