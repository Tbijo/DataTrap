package com.example.datatrap.specie.data.specie_image

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SpecieImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(specieImageEntity: SpecieImageEntity)

    @Delete
    suspend fun deleteImage(specieImageEntity: SpecieImageEntity)

    @Query("SELECT * FROM SpecieImageEntity WHERE specieID = :specieId")
    suspend fun getImageForSpecie(specieId: String): SpecieImageEntity?
}