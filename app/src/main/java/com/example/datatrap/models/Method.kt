package com.example.datatrap.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "methods")
data class Method(

    @PrimaryKey(autoGenerate = true)
    var methodId: Long,

    @ColumnInfo(name = "MethodName")
    var methodName: String,

    var methodDateTimeCreated: Date,

    var methodDateTimeUpdated: Date?
)
