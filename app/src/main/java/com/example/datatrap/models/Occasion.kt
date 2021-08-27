package com.example.datatrap.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "occasion")
data class Occasion(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "Occasion")
    val occasion: Int,

    @ColumnInfo(name = "LocalityName") // cudzi kluc
    val locality: String,

    @ColumnInfo(name = "Session") // cudzi kluc
    val session: Long,

    @ColumnInfo(name = "MethodName") // cudzi kluc
    val method: String,

    @ColumnInfo(name = "MethodTypeName") // cudzi kluc
    val methodType: String,

    @ColumnInfo(name = "TrapTypeName") // cudzi kluc
    val trapType: String,

    @ColumnInfo(name = "EnvTypeName") // cudzi kluc
    val envType: String?,

    @ColumnInfo(name = "VegetTypeName") // cudzi kluc
    val vegetType: String?,

    @ColumnInfo(name = "Date")
    val date: String,

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

    @ColumnInfo(name = "Img_occ")
    val img: String?
): Parcelable
