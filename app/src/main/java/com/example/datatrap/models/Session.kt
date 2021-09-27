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
@Entity(tableName = "sessions", foreignKeys = [
    ForeignKey(entity = Project::class, parentColumns = ["projectId"], childColumns = ["projectID"], onDelete = CASCADE)
])
data class Session(

    @PrimaryKey(autoGenerate = true)
    var sessionId: Long,

    @ColumnInfo(name = "Session")
    var session: Int,

    var deviceID: String,
    
    // cudzi kluc
    var projectID: Long?,

    @ColumnInfo(name = "Num_occasion")
    var numOcc: Int,

    var sessionDateTime: Date
): Parcelable
