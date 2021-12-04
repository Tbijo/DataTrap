package com.example.datatrap.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class SynchronizeDate(

    @PrimaryKey(autoGenerate = false)
    var lastSyncId: Long,

    var lastSyncDate: Date?
)
