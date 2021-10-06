package com.example.datatrap.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "species")
data class Specie(

    @PrimaryKey(autoGenerate = true)
    var specieId: Long,

    @ColumnInfo(name = "Species_code")
    var speciesCode: String,

    @ColumnInfo(name = "Full_name")
    var fullName: String,

    @ColumnInfo(name = "Synonym")
    var synonym: String?,

    @ColumnInfo(name = "Authority")
    var authority: String,

    @ColumnInfo(name = "Description")
    var description: String?,

    @ColumnInfo(name = "Is_small_mammal")
    var isSmallMammal: Int, //boolean

    @ColumnInfo(name = "Upper_fingers")
    var upperFingers: Int?,

    @ColumnInfo(name = "Min_weight")
    var minWeight: Float?, //kontrloly pri vstupoch

    @ColumnInfo(name = "Max_weight")
    var maxWeight: Float?, //kontrloly pri vstupoch

    @ColumnInfo(name = "Note_sp")
    var note: String?,

    @ColumnInfo(name = "Img_sp")
    var imgName: String?,

    var specieDateTimeCreated: Date,

    var specieDateTimeUpdated: Date?
): Parcelable
