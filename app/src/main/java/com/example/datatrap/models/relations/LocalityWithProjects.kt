package com.example.datatrap.models.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.datatrap.models.Locality
import com.example.datatrap.models.Project

data class LocalityWithProjects(

    @Embedded
    val locality: Locality,

    @Relation(
        parentColumn = "localityId",
        entityColumn = "projectId",
        associateBy = Junction(ProjectLocalityCrossRef::class)
    )
    val projects: List<Project>

)
