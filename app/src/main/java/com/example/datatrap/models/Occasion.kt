package com.example.datatrap.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "occasions")
data class Occasion(

    @PrimaryKey(autoGenerate = true)
    val occasionId: Long,

    @ColumnInfo(name = "Occasion")
    val occasion: Int,

    // cudzi kluc
    val localityId: Long,

    // cudzi kluc
    val sessionId: Long,

    // cudzi kluc
    val methodId: Long,

    // cudzi kluc
    val methodTypeId: Long,

    // cudzi kluc
    val trapTypeId: Long,

    // cudzi kluc
    val envTypeId: Long?,

    // cudzi kluc
    val vegetTypeId: Long?,

    @ColumnInfo(name = "Date")
    val date: String,

    @ColumnInfo(name = "Time")
    val time: String,

    @ColumnInfo(name = "Got_caught")
    val gotCaught: Int?, // boolean

    @ColumnInfo(name = "No_traps")
    val numTraps: Int?,

    @ColumnInfo(name = "Num_mice")
    val numMice: Int?,

    @ColumnInfo(name = "Temperature")
    val temperature: Float?,

    @ColumnInfo(name = "Weather")
    val weather: String?,

    @ColumnInfo(name = "Leg")
    val leg: String,

    @ColumnInfo(name = "Note_occ")
    val note: String?,

    // cudzi kluc
    @ColumnInfo(name = "Img_occ")
    val imgName: String?
): Parcelable
