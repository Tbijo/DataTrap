package com.example.datatrap.project.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.project.data.Project

@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project)

    @Update
    suspend fun updateProject(project: Project)

    @Delete
    suspend fun deleteProject(project: Project)

    @Query("SELECT * FROM Project")
    fun getProjects(): LiveData<List<Project>>

    @Query("SELECT * FROM Project WHERE ProjectName LIKE :nameProject")
    fun searchProjects(nameProject: String): LiveData<List<Project>>

    @Query("SELECT * FROM project WHERE projectId IN (:projectIds)")
    suspend fun getProjectForSync(projectIds: List<Long>): List<Project>

    // Sync
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncProject(project: Project): Long

    @Query("SELECT * FROM project WHERE projectName = :projectName")
    suspend fun getProjectByName(projectName: String): Project?
}