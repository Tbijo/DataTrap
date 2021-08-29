package com.example.datatrap.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "methods")
data class Method(

    @PrimaryKey(autoGenerate = true)
    val methodId: Long,

    @ColumnInfo(name = "MethodName")
    val methodName: String
)
