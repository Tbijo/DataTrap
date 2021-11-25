package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.datatrap.models.SpecieImage

@Dao
interface SpecieImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(specieImage: SpecieImage)

    @Query("DELETE FROM SpecieImage WHERE specieImgId = :specieImgId")
    suspend fun deleteImage(specieImgId: Long)

    @Query("SELECT * FROM SpecieImage WHERE specieID = :specieId")
    fun getImageForSpecie(specieId: Long): LiveData<SpecieImage>
}