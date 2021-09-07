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
    var localityId: Long,

    @ColumnInfo(name = "LocalityName")
    var localityName: String,

    @ColumnInfo(name = "Date")
    var date: String,

    @ColumnInfo(name = "X")
    var x: Float, // latitude

    @ColumnInfo(name = "Y")
    var y: Float, // longitude

    @ColumnInfo(name = "Num_ses")
    var numSessions: Int,

    @ColumnInfo(name = "Note_loc")
    var note: String?
): Parcelable
