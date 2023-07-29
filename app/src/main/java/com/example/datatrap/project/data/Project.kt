package com.example.datatrap.project.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity
data class Project(

    @PrimaryKey(autoGenerate = true)
    var projectId: Long,

    var projectName: String,

    var projectDateTimeCreated: Date,

    var projectDateTimeUpdated: Date?,

    var numLocal: Int,

    var numMice: Int,

    var projectStart: Long
): Parcelable
