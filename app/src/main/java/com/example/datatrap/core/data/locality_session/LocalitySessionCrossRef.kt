package com.example.datatrap.core.data.locality_session

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import com.example.datatrap.locality.data.locality.LocalityEntity
import com.example.datatrap.session.data.SessionEntity

@Entity(primaryKeys = ["localityId", "sessionId"], foreignKeys = [
    ForeignKey(entity = LocalityEntity::class, parentColumns = ["localityId"], childColumns = ["localityId"], onDelete = CASCADE),
    ForeignKey(entity = SessionEntity::class, parentColumns = ["sessionId"], childColumns = ["sessionId"], onDelete = CASCADE)
])
data class LocalitySessionCrossRef(

    @ColumnInfo(index = true)
    val localityId: String,

    @ColumnInfo(index = true)
    val sessionId: String,
)