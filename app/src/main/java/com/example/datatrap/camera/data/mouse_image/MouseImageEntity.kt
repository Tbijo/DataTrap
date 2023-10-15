package com.example.datatrap.camera.data.mouse_image

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.example.datatrap.mouse.data.MouseEntity
import java.time.ZonedDateTime
import java.util.UUID

@Entity(foreignKeys = [
    ForeignKey(entity = MouseEntity::class, parentColumns = ["mouseId"], childColumns = ["mouseID"], onDelete = CASCADE)
])
data class MouseImageEntity(

    @PrimaryKey
    val mouseImgId: String = UUID.randomUUID().toString(),

    val imgName: String,

    val path: String,

    val note: String?,

    @ColumnInfo(index = true)
    val mouseID: String,
    
    val dateTimeCreated: ZonedDateTime,

    val dateTimeUpdated: ZonedDateTime?,
)
