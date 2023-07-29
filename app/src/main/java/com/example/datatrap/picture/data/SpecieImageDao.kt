package com.example.datatrap.picture.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.datatrap.picture.data.SpecieImage
import com.example.datatrap.sync.data.sync.SpecieImageSync

@Dao
interface SpecieImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(specieImage: SpecieImage)

    @Query("DELETE FROM SpecieImage WHERE specieImgId = :specieImgId")
    suspend fun deleteImage(specieImgId: Long)

    @Query("SELECT * FROM SpecieImage WHERE specieID = :specieId")
    fun getImageForSpecie(specieId: Long): LiveData<SpecieImage>

    @Query("SELECT imgName, path, note, specieID, uniqueCode FROM SpecieImage WHERE uniqueCode >= :unixTime")
    suspend fun getSpecieImages(unixTime: Long): List<SpecieImageSync>
}