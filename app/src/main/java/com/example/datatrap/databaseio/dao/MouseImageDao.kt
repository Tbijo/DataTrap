package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.MouseImage

@Dao
interface MouseImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(mouseImage: MouseImage)

    @Query("DELETE FROM MouseImage WHERE mouseImgId = :mouseImgId")
    suspend fun deleteImage(mouseImgId: Long)

    @Query("SELECT * FROM MouseImage WHERE mouseID = :mouseId")
    fun getImageForMouse(mouseId: Long): LiveData<MouseImage>
}