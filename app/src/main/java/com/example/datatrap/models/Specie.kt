package com.example.datatrap.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "species")
data class Specie(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "Species_code")
    val speciesCode: String,

    @ColumnInfo(name = "Full_name")
    val fullName: String,

    @ColumnInfo(name = "Synonym")
    val synonym: String?,

    @ColumnInfo(name = "Authority")
    val authority: String,

    @ColumnInfo(name = "Description")
    val description: String?,

    @ColumnInfo(name = "Is_small_mammal")
    val isSmallMammal: Int, //boolean

    @ColumnInfo(name = "Upper_fingers")
    val upperFingers: Int?,

    @ColumnInfo(name = "Min_weight")
    val minWeight: Float?, //kontrloly pri vstupoch

    @ColumnInfo(name = "Max_weight")
    val maxWeight: Float?, //kontrloly pri vstupoch

    @ColumnInfo(name = "Note_sp")
    val note: String?,

    @ColumnInfo(name = "Img_sp")
    val img: Int?
): Parcelable
