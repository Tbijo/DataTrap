package com.example.datatrap.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class Picture(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "Img_ID")
    val imgID: String,

    @ColumnInfo(name = "Path")
    val path: String,

    @ColumnInfo(name = "Note_img")
    val note: String?
)
