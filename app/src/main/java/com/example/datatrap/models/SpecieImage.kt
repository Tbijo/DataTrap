package com.example.datatrap.models

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import okhttp3.MultipartBody

@Entity(foreignKeys = [
    ForeignKey(entity = Specie::class, parentColumns = ["specieId"], childColumns = ["specieID"], onDelete = CASCADE)
])
data class SpecieImage(

    @PrimaryKey(autoGenerate = true)
    var specieImgId: Long,

    var imgName: String,

    var path: String,

    var note: String?,

    @ColumnInfo(index = true)
    var specieID: Long,

    var uniqueCode: Long
)
