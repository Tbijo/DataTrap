package com.example.datatrap.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "veget_types")
data class VegetType(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "VegetTypeName")
    val vegetTypeName: String
)
