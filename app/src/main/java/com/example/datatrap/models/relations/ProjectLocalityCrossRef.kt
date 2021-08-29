package com.example.datatrap.models.relations

import androidx.room.Entity

// len v pripade many-to-many tabulky anotujem triedu lebo vytvaram samostatnu tabulku a budem davat do nej data
// treba nastavit entity z dvoma klucmi
@Entity(primaryKeys = ["projectId", "localityId"])
data class ProjectLocalityCrossRef(
    // bude obsahovat primarne kluce oboch tabuliek
    // ani jeden z nich vsak nebude primarny kluc lebo ich kombinacia bude

    val projectId: Long, // primarny kluc tabulky project
    val localityId: Long // primarny kluc tabulky locality

    // ak chceme vsetky projekty ktore su vo vztahu s lokalitou treba na to pomocnu triedu      -   LocalityWithProjects
    // ak chceme vsetky lokality ktore su vo vztahu s projektom treba na to tiez pomocnu triedu -   ProjectWithLocalities - ja chcem len toto
)
