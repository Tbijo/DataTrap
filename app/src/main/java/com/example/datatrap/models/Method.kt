package com.example.datatrap.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Method(

    @PrimaryKey(autoGenerate = true)
    var methodId: Long,

    var methodName: String,

    var methodDateTimeCreated: Date,

    var methodDateTimeUpdated: Date?
)
