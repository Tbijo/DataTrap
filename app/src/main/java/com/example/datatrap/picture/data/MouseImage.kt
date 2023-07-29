package com.example.datatrap.picture.data

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.example.datatrap.mouse.data.Mouse

@Entity(foreignKeys = [
    ForeignKey(entity = Mouse::class, parentColumns = ["mouseId"], childColumns = ["mouseIiD"], onDelete = CASCADE)
])
data class MouseImage(

    @PrimaryKey(autoGenerate = true)
    var mouseImgId: Long,

    var imgName: String,

    var path: String,

    var note: String?,

    @ColumnInfo(index = true)
    var mouseIiD: Long,

    var deviceID: String,

    var uniqueCode: Long
)
