package com.example.datatrap.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "env_types")
data class EnvType(

    @PrimaryKey(autoGenerate = true)
    val envTypeId: Long,

    @ColumnInfo(name = "EnvTypeName")
    val envTypeName: String
)
