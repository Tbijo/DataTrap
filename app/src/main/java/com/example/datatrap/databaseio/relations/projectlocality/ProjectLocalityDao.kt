package com.example.datatrap.databaseio.relations.projectlocality

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectLocalityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProjectLocalityCrossRef(projectLocalityCrossRef: ProjectLocalityCrossRef)

    @Delete
    suspend fun deleteProjectLocalityCrossRef(projectLocalityCrossRef: ProjectLocalityCrossRef)

    @Transaction // kedze robime pristup k viacerym tabulkam treba anotovat aby nedoslo k viacerym pristupom naraz
    @Query("SELECT * FROM projects WHERE ProjectName = :projectName")
    fun getLocalitiesForProject(projectName: String): Flow<List<ProjectWithLocalities>>
}