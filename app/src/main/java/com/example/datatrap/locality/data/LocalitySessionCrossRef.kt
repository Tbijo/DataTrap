package com.example.datatrap.locality.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import com.example.datatrap.session.data.Session

@Entity(primaryKeys = ["localityId", "sessionId"], foreignKeys = [
    ForeignKey(entity = Locality::class, parentColumns = ["localityId"], childColumns = ["localityId"], onDelete = CASCADE),
    ForeignKey(entity = Session::class, parentColumns = ["sessionId"], childColumns = ["sessionId"], onDelete = CASCADE)
])
data class LocalitySessionCrossRef(

    @ColumnInfo(index = true)
    var localityId: Long,

    @ColumnInfo(index = true)
    var sessionId: Long
)