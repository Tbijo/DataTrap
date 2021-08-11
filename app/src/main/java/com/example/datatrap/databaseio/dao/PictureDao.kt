package com.example.datatrap.databaseio.dao

import android.graphics.Picture
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PictureDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPicture(picture: Picture)

    @Query("SELECT * FROM images WHERE Img_ID = :pictureID")
    suspend fun getPicture(pictureID: String): Picture

    // mozno este jedna metoda getPicture ale vstup param bude path obrazka
}