package com.example.datatrap.databaseio.dao

import androidx.room.*
import com.example.datatrap.models.Picture
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPicture(picture: Picture)

    @Delete
    suspend fun deletePicture(picture: Picture)

    @Query("SELECT * FROM images WHERE Img_ID = :pictureID")
    fun getPicture(pictureID: String): Flow<List<Picture>>

    // mozno este jedna metoda getPicture ale vstup param bude path obrazka
}