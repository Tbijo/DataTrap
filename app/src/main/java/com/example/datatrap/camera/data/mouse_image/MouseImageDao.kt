package com.example.datatrap.camera.data.mouse_image

import androidx.room.*

@Dao
interface MouseImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(mouseImageEntity: MouseImageEntity)

    @Delete
    suspend fun deleteImage(mouseImageEntity: MouseImageEntity)

    @Query("SELECT * FROM MouseImageEntity WHERE mouseID = :mouseID")
    suspend fun getImageForMouse(mouseID: String): MouseImageEntity?
}