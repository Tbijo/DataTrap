package com.example.datatrap.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(foreignKeys = [
    ForeignKey(entity = Project::class, parentColumns = ["projectId"], childColumns = ["projectID"], onDelete = CASCADE)
])
data class Session(

    @PrimaryKey(autoGenerate = true)
    var sessionId: Long,

    var session: Int,
    
    // cudzi kluc
    @ColumnInfo(index = true)
    var projectID: Long?,

    var numOcc: Int,

    var sessionDateTimeCreated: Date,

    var sessionDateTimeUpdated: Date?,

    var sessionStart: Long
): Parcelable
