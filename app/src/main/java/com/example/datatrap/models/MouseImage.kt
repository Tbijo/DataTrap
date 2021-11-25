package com.example.datatrap.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(entity = Mouse::class, parentColumns = ["mouseId"], childColumns = ["mouseID"], onDelete = CASCADE)
])
data class MouseImage(

    @PrimaryKey(autoGenerate = true)
    var mouseImgId: Long,

    var imgName: String,

    var path: String,

    var note: String?,

    @ColumnInfo(index = true)
    var mouseID: Long,

    var deviceID: String

)
