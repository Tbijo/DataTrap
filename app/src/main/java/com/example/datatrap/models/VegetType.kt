package com.example.datatrap.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class VegetType(

    @PrimaryKey(autoGenerate = true)
    var vegetTypeId: Long,

    var vegetTypeName: String,

    var vegTypeDateTimeCreated: Date,

    var vegTypeDateTimeUpdated: Date?
)
