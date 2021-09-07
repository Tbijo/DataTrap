package com.example.datatrap.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "veget_types")
data class VegetType(

    @PrimaryKey(autoGenerate = true)
    var vegetTypeId: Long,

    @ColumnInfo(name = "VegetTypeName")
    var vegetTypeName: String
)
