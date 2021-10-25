package com.example.datatrap.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class User(

    @PrimaryKey(autoGenerate = true)
    var userId: Long,

    var userName: String,

    var password: String,

    var team: Int,

    var isActive: Int
): Parcelable
