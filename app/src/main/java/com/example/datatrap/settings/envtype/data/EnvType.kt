package com.example.datatrap.settings.envtype.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class EnvType(

    @PrimaryKey(autoGenerate = true)
    var envTypeId: Long,

    var envTypeName: String,

    var envTypeDateTimeCreated: Date,

    var envTypeDateTimeUpdated: Date?
)
