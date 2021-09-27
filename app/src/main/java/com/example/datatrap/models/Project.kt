package com.example.datatrap.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "projects")
data class Project(

    @PrimaryKey(autoGenerate = true)
    var projectId: Long,

    @ColumnInfo(name = "ProjectName")
    var projectName: String,

    var deviceID: String,

    var projectDateTime: Date,

    @ColumnInfo(name = "Num_local")
    var numLocal: Int,

    @ColumnInfo(name = "Num_mice")
    var numMice: Int
): Parcelable
