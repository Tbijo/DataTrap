package com.example.datatrap.camera.data.mouse_image

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.example.datatrap.mouse.data.MouseEntity
import java.util.UUID

@Entity(foreignKeys = [
    ForeignKey(entity = MouseEntity::class, parentColumns = ["mouseId"], childColumns = ["mouseIiD"], onDelete = CASCADE)
])
data class MouseImageEntity(

    @PrimaryKey
    var mouseImgId: String = UUID.randomUUID().toString(),

    var imgName: String,

    var path: String,

    var note: String?,

    @ColumnInfo(index = true)
    var mouseIiD: String,

    var deviceID: String,

    var uniqueCode: Long
)
