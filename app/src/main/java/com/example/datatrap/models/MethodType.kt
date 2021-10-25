package com.example.datatrap.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class MethodType(

    @PrimaryKey(autoGenerate = true)
    var methodTypeId: Long,

    var methodTypeName: String,

    var methTypeDateTimeCreated: Date,

    var methTypeDateTimeUpdated: Date?
)
