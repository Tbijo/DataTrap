package com.example.datatrap.camera.data.mouse_image

import androidx.room.*
import com.example.datatrap.sync.data.MouseImageSync
import kotlinx.coroutines.flow.Flow

@Dao
interface MouseImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(mouseImageEntity: MouseImageEntity)

    @Query("DELETE FROM MouseImageEntity WHERE mouseImgId = :mouseImgId")
    suspend fun deleteImage(mouseImgId: Long)

    @Query("SELECT * FROM MouseImageEntity WHERE mouseIiD = :mouseIid AND deviceID = :deviceID")
    fun getImageForMouse(mouseIid: Long, deviceID: String): Flow<MouseImageEntity>

    @Query("SELECT imgName, path, note, mouseIiD, deviceID, uniqueCode FROM MouseImageEntity WHERE uniqueCode >= :unixTime")
    suspend fun getMouseImages(unixTime: Long): List<MouseImageSync>
}