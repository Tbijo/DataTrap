package com.example.datatrap.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Picture(

    @PrimaryKey(autoGenerate = false)
    var imgName: String,

    var path: String,

    var note: String?
)
