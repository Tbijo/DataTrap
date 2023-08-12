package com.example.datatrap.specie.data.specie_image

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.example.datatrap.specie.data.SpecieEntity
import java.util.UUID

@Entity(foreignKeys = [
    ForeignKey(entity = SpecieEntity::class, parentColumns = ["specieId"], childColumns = ["specieID"], onDelete = CASCADE)
])
data class SpecieImageEntity(

    @PrimaryKey
    var specieImgId: String = UUID.randomUUID().toString(),

    var imgName: String,

    var path: String,

    var note: String?,

    @ColumnInfo(index = true)
    var specieID: String,

    var uniqueCode: Long
)
