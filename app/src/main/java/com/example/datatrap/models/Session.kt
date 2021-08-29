package com.example.datatrap.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "sessions")
data class Session(
    
    @PrimaryKey(autoGenerate = true)
    val sessionId: Long,

    @ColumnInfo(name = "Session")
    val session: Int,
    
    // cudzi kluc
    val projectId: Long?,

    @ColumnInfo(name = "Num_occasion")
    val numOcc: Int,

    @ColumnInfo(name = "Date")
    val date: String
): Parcelable
