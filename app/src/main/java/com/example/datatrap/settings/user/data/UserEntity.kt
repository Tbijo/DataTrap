package com.example.datatrap.settings.user.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class UserEntity(

    @PrimaryKey
    var userId: String = UUID.randomUUID().toString(),

    var userName: String,

    var password: String
)
