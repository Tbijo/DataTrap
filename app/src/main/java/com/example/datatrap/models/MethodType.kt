package com.example.datatrap.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "method_types")
data class MethodType(

    @PrimaryKey(autoGenerate = true)
    var methodTypeId: Long,

    @ColumnInfo(name = "MethodTypeName")
    var methodTypeName: String
)
