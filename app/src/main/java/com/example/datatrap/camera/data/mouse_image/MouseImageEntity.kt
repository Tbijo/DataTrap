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
    var mouseImgId: String = UUID.randomUUID().toString(),

    var imgName: String,

    var path: String,

    var note: String?,

    @ColumnInfo(index = true)
    var mouseID: String,
    
    var dateTimeCreated: ZonedDateTime,

    var dateTimeUpdated: ZonedDateTime?,
)
