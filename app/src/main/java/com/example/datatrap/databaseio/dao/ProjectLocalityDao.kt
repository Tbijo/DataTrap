package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.relations.ProjectLocalityCrossRef
import com.example.datatrap.models.relations.ProjectWithLocalities

@Dao
interface ProjectLocalityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjectLocalityCrossRef(projectLocalityCrossRef: ProjectLocalityCrossRef)

    @Delete
    suspend fun deleteProjectLocalityCrossRef(projectLocalityCrossRef: ProjectLocalityCrossRef)

    @Transaction // kedze robime pristup k viacerym tabulkam treba anotovat aby nedoslo k viacerym pristupom naraz
    @Query("SELECT * FROM projects WHERE ProjectName = :projectName")
    fun getLocalitiesForProject(projectName: String): LiveData<List<ProjectWithLocalities>>
}