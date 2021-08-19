package com.example.datatrap.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "projects")
data class Project(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "ProjectName")
    val projectName: String,

    @ColumnInfo(name = "Date")
    val date: String,

    @ColumnInfo(name = "Num_local")
    val numLocal: Int,

    @ColumnInfo(name = "Num_mice")
    val numMice: Int
): Parcelable
