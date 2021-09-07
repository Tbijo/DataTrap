package com.example.datatrap.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(

    @PrimaryKey(autoGenerate = true)
    var userId: Long,

    var userName: String,

    var password: String,

    var team: Int
)
