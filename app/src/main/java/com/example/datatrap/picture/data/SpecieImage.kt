package com.example.datatrap.picture.data

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.example.datatrap.specie.data.Specie

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
