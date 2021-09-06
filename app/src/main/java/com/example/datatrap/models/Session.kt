package com.example.datatrap.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "sessions", foreignKeys = [
    ForeignKey(entity = Project::class, parentColumns = ["projectId"], childColumns = ["projectID"], onDelete = CASCADE)
])
data class Session(

    @PrimaryKey(autoGenerate = true)
    val sessionId: Long,

    @ColumnInfo(name = "Session")
    val session: Int,
    
    // cudzi kluc
    val projectID: Long?,

    @ColumnInfo(name = "Num_occasion")
    val numOcc: Int,

    @ColumnInfo(name = "Date")
    val date: String
): Parcelable
