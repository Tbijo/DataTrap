package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.Project

@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project)

    @Update
    suspend fun updateProject(project: Project)

    @Delete
    suspend fun deleteProject(project: Project)

    @Query("SELECT * FROM projects WHERE projectId = :projectId")
    fun getProject(projectId: Long): LiveData<Project>

    @Query("SELECT * FROM projects")
    fun getProjects(): LiveData<List<Project>>

    @Query("SELECT * FROM projects WHERE ProjectName LIKE :nameProject")
    fun searchProjects(nameProject: String): LiveData<List<Project>>
}