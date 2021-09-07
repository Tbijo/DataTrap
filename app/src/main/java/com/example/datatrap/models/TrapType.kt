package com.example.datatrap.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trap_types")
data class TrapType(

    @PrimaryKey(autoGenerate = true)
    var trapTypeId: Long,

    @ColumnInfo(name = "TrapTypeName")
    var trapTypeName: String
)
