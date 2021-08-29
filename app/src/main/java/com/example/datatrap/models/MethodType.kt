package com.example.datatrap.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "method_types")
data class MethodType(

    @PrimaryKey(autoGenerate = true)
    val methodTypeId: Long,

    @ColumnInfo(name = "MethodTypeName")
    val methodTypeName: String
)
