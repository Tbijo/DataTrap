package com.example.datatrap.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locality")
data class Locality(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "LocalityName")
    val localityName: String,

    @ColumnInfo(name = "Date")
    val date: String,

    @ColumnInfo(name = "X")
    val x: Float, // latitude

    @ColumnInfo(name = "Y")
    val y: Float, // longitude

    @ColumnInfo(name = "Num_ses")
    val numSessions: Int,

    @ColumnInfo(name = "Note_loc")
    val note: String?
)
