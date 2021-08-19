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
    val id: Long,

    @ColumnInfo(name = "Session")
    val session: Int,
    
    @ColumnInfo(name = "ProjectName") // cudzi kluc
    val projectName: String?,

    @ColumnInfo(name = "Num_occasion")
    val numOcc: Int,

    @ColumnInfo(name = "Date")
    val date: String
): Parcelable
