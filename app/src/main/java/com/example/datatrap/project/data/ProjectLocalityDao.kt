package com.example.datatrap.project.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProjectLocalityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjectLocalityCrossRef(projectLocalityCrossRef: ProjectLocalityCrossRef)

    @Delete
    suspend fun deleteProjectLocalityCrossRef(projectLocalityCrossRef: ProjectLocalityCrossRef)

    @Transaction // kedze robime pristup k viacerym tabulkam treba anotovat aby nedoslo k viacerym pristupom naraz
    @Query("SELECT * FROM Project WHERE projectId = :projectId")
    fun getLocalitiesForProject(projectId: Long): LiveData<List<ProjectWithLocalities>>
}