package com.example.datatrap.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "localities")
data class Locality(

    @PrimaryKey(autoGenerate = true)
    val localityId: Long,

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
): Parcelable
