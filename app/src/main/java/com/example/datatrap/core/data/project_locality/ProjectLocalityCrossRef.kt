package com.example.datatrap.core.data.project_locality

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import com.example.datatrap.locality.data.locality.LocalityEntity
import com.example.datatrap.project.data.ProjectEntity

@Entity(primaryKeys = ["projectId", "localityId"], foreignKeys = [
    ForeignKey(entity = ProjectEntity::class, parentColumns = ["projectId"], childColumns = ["projectId"], onDelete = CASCADE),
    ForeignKey(entity = LocalityEntity::class, parentColumns = ["localityId"], childColumns = ["localityId"], onDelete = CASCADE)
])
data class ProjectLocalityCrossRef(

    @ColumnInfo(index = true)
    val projectId: String,

    @ColumnInfo(index = true)
    val localityId: String,
)