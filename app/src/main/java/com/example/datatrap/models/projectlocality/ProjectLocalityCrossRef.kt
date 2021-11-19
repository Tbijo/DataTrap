package com.example.datatrap.models.projectlocality

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import com.example.datatrap.models.Locality
import com.example.datatrap.models.Project

// len v pripade many-to-many tabulky anotujem triedu lebo vytvaram samostatnu tabulku a budem davat do nej data
// treba nastavit entity z dvoma klucmi
@Entity(primaryKeys = ["projectId", "localityId"], foreignKeys = [
    ForeignKey(entity = Project::class, parentColumns = ["projectId"], childColumns = ["projectId"], onDelete = CASCADE),
    ForeignKey(entity = Locality::class, parentColumns = ["localityId"], childColumns = ["localityId"], onDelete = CASCADE)
])
data class ProjectLocalityCrossRef(
    // bude obsahovat primarne kluce oboch tabuliek
    // ani jeden z nich vsak nebude primarny kluc lebo ich kombinacia bude

    var projectId: Long, // primarny kluc tabulky project
    var localityId: Long // primarny kluc tabulky locality

    // ak chceme vsetky projekty ktore su vo vztahu s lokalitou treba na to pomocnu triedu      -   LocalityWithProjects
    // ak chceme vsetky lokality ktore su vo vztahu s projektom treba na to tiez pomocnu triedu -   ProjectWithLocalities - ja som chcel len toto
)
