package com.example.datatrap.databaseio.dao

import androidx.room.*
import com.example.datatrap.models.Picture

@Dao
interface PictureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPicture(picture: Picture)

    @Delete
    suspend fun deletePicture(picture: Picture)

    @Query("SELECT * FROM images WHERE Img_name = :imgName")
    suspend fun getPictureById(imgName: String): Picture?
}