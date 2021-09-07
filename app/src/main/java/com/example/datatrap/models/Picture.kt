package com.example.datatrap.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class Picture(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "Img_name")
    var imgName: String,

    @ColumnInfo(name = "Path")
    var path: String,

    @ColumnInfo(name = "Note_img")
    var note: String?
)
