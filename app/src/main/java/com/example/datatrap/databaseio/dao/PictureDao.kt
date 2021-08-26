package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.Picture

@Dao
interface PictureDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPicture(picture: Picture)

    @Delete
    suspend fun deletePicture(picture: Picture)

    @Query("SELECT * FROM images WHERE Img_ID = :pictureID")
    fun getPictureById(pictureID: String): LiveData<Picture>
}