package com.example.datatrap.databaseio.relations.projectlocality

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.datatrap.models.Locality
import com.example.datatrap.models.Project

// sluzi ako pomocna trieda preto z nej nerobime entitu ako samostatnu tabulku
data class ProjectWithLocalities(

    // embeded dame pre tabulku ktora obsahuje v sebe akoby instancie druhej tabulky
    @Embedded
    val project: Project,

    // relation definujem v nom primarne kluce oboch tabuliek - na ktorych stlpcoch sa maju prepojit
    @Relation(
        parentColumn = "ProjectName", // primarny kluc tabulky Projects
        entityColumn = "LocalityName", // cudzi kluc tabulky Locality

        // kedze tato relacia ma vlastnu tabulku N:M
        // treba povedat Room ktora tabulka specifikuje tuto relaciu (ProjectLocalityCrossRef)
        // Room potrebuje vediet v ktorej tabulke najde spojku medzi tymi dvoma tabulkami
        associateBy = Junction(ProjectLocalityCrossRef::class)
    )
    // narozdiel od one to one treba sem dat list hodnot lebo jeden project moze mat viacero lokalit
    val localities: List<Locality>

)
