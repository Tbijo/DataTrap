package com.example.datatrap.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "protocols")
data class Protocol(

    @PrimaryKey(autoGenerate = true)
    var protocolId: Long,

    @ColumnInfo(name = "ProtocolName")
    var protocolName: String,

    var protDateTimeCreated: Date,

    var protDateTimeUpdated: Date?
)
