package com.example.datatrap.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Protocol(

    @PrimaryKey(autoGenerate = true)
    var protocolId: Long,

    var protocolName: String,

    var protDateTimeCreated: Date,

    var protDateTimeUpdated: Date?
)
