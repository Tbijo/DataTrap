package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.MouseImage
import com.example.datatrap.models.sync.MouseImageSync

@Dao
interface MouseImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(mouseImage: MouseImage)

    @Query("DELETE FROM MouseImage WHERE mouseImgId = :mouseImgId")
    suspend fun deleteImage(mouseImgId: Long)

    @Query("SELECT * FROM MouseImage WHERE mouseIiD = :mouseIid AND deviceID = :deviceID")
    fun getImageForMouse(mouseIid: Long, deviceID: String): LiveData<MouseImage>

    @Query("SELECT imgName, path, note, mouseIiD, deviceID, uniqueCode FROM MouseImage WHERE uniqueCode >= :unixTime")
    suspend fun getMouseImages(unixTime: Long): List<MouseImageSync>
}