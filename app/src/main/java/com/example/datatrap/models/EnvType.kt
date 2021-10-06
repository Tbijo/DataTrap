package com.example.datatrap.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "env_types")
data class EnvType(

    @PrimaryKey(autoGenerate = true)
    var envTypeId: Long,

    @ColumnInfo(name = "EnvTypeName")
    var envTypeName: String,

    var envTypeDateTimeCreated: Date,

    var envTypeDateTimeUpdated: Date?
)
