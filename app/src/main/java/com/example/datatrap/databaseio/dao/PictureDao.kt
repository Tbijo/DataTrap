package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.Picture

@Dao
interface PictureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPicture(picture: Picture)

    @Delete
    suspend fun deletePicture(picture: Picture)

    @Query("SELECT * FROM Picture WHERE imgName = :imgName")
    suspend fun getPictureById(imgName: String): Picture
}