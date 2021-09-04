package com.example.datatrap.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "sm")
data class Mouse(
    
    @PrimaryKey(autoGenerate = true)
    val mouseId: Long,

    @ColumnInfo(name = "Code")
    val code: Int?, //pazure

    // cudzi kluc
    val speciesID: Long,

    // cudzi kluc
    val protocolID: Long?,

    // cudzi kluc
    val occasionID: Long,

    // cudzi kluc
    val localityID: Long,

    @ColumnInfo(name = "Trap_ID")
    val trapID: Int, //samy zadavat

    @ColumnInfo(name = "Date")
    val date: String,

    @ColumnInfo(name = "Catch_time")
    val catchTime: String?,

    @ColumnInfo(name = "Sex")
    val sex: String?, //list male female null

    @ColumnInfo(name = "Age")
    val age: String?, //vytvor list hodnot

    @ColumnInfo(name = "Gravidity")
    val gravidity: Int?, //bool

    @ColumnInfo(name = "Lactating")
    val lactating: Int?, //bool

    @ColumnInfo(name = "Sex_active")
    val sexActive: Int?, //bool

    @ColumnInfo(name = "Weight")
    val weight: Float?,

    @ColumnInfo(name = "Recapture")
    val recapture: Int?, //bool

    @ColumnInfo(name = "Capture_ID")
    val captureID: String?, //vytvor list hodnot

    @ColumnInfo(name = "Body")
    val body: Float?,

    @ColumnInfo(name = "Tail")
    val tail: Float?,

    @ColumnInfo(name = "Feet")
    val feet: Float?,

    @ColumnInfo(name = "Ear")
    val ear: Float?,

    @ColumnInfo(name = "Testes_length")
    val testesLength: Float?,

    @ColumnInfo(name = "Testes_width")
    val testesWidth: Float?,

    @ColumnInfo(name = "Embryo_right")
    val embryoRight: Int?,

    @ColumnInfo(name = "Embryo_left")
    val embryoLeft: Int?,

    @ColumnInfo(name = "Embryo_diameter")
    val embryoDiameter: Float?,

    val MC: Int?, //bool

    @ColumnInfo(name = "MC_right")
    val MCright: Int?,

    @ColumnInfo(name = "MC_left")
    val MCleft: Int?,

    @ColumnInfo(name = "Note_sm")
    val note: String?,

    @ColumnInfo(name = "Img_sm") // cudzi kluc
    val imgName: String?,
): Parcelable
