package com.example.datatrap.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "method_types")
data class MethodType(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "MethodTypeName")
    val methodTypeName: String
)
