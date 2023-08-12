package com.example.datatrap.specie.data.specie_image

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.datatrap.sync.data.SpecieImageSync

@Dao
interface SpecieImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(specieImageEntity: SpecieImageEntity)

    @Query("DELETE FROM SpecieImageEntity WHERE specieImgId = :specieImgId")
    suspend fun deleteImage(specieImgId: Long)

    @Query("SELECT * FROM SpecieImageEntity WHERE specieID = :specieId")
    fun getImageForSpecie(specieId: Long): LiveData<SpecieImageEntity>

    @Query("SELECT imgName, path, note, specieID, uniqueCode FROM SpecieImageEntity WHERE uniqueCode >= :unixTime")
    suspend fun getSpecieImages(unixTime: Long): List<SpecieImageSync>
}