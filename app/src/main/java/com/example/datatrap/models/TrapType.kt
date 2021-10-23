package com.example.datatrap.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
data class TrapType(

    @PrimaryKey(autoGenerate = true)
    var trapTypeId: Long,

    var trapTypeName: String,

    var trapTypeDateTimeCreated: Date,

    var trapTypeDateTimeUpdated: Date?
)
