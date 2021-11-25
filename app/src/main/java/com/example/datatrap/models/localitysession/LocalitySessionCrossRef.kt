package com.example.datatrap.models.localitysession

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import com.example.datatrap.models.Locality
import com.example.datatrap.models.Session

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