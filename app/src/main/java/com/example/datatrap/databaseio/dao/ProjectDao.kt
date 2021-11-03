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

    @Query("SELECT * FROM Project WHERE projectId = :projectId")
    suspend fun getProject(projectId: Long): Project

    @Query("SELECT * FROM Project")
    fun getProjects(): LiveData<List<Project>>

    @Query("SELECT * FROM Project WHERE ProjectName LIKE :nameProject")
    fun searchProjects(nameProject: String): LiveData<List<Project>>
}