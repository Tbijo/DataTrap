package com.example.datatrap.databaseio.dao

import androidx.room.*
import com.example.datatrap.models.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProject(project: Project)

    @Update
    suspend fun updateProject(project: Project)

    @Delete
    suspend fun deleteProject(project: Project)

    @Query("SELECT * FROM projects WHERE ProjectName = :projectName")
    suspend fun getProject(projectName: String)

    @Query("SELECT * FROM projects")
    fun getProjects(): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE ProjectName LIKE :nameProject")
    fun searchProjects(nameProject: String): Flow<List<Project>>
}