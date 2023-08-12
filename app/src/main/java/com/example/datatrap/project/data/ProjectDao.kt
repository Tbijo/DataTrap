package com.example.datatrap.project.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(projectEntity: ProjectEntity)

    @Update
    suspend fun updateProject(projectEntity: ProjectEntity)

    @Delete
    suspend fun deleteProject(projectEntity: ProjectEntity)

    @Query("SELECT * FROM ProjectEntity")
    fun getProjects(): LiveData<List<ProjectEntity>>

    @Query("SELECT * FROM ProjectEntity WHERE ProjectName LIKE :nameProject")
    fun searchProjects(nameProject: String): LiveData<List<ProjectEntity>>

    @Query("SELECT * FROM project WHERE projectId IN (:projectIds)")
    suspend fun getProjectForSync(projectIds: List<Long>): List<ProjectEntity>

    // Sync
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncProject(projectEntity: ProjectEntity): Long

    @Query("SELECT * FROM project WHERE projectName = :projectName")
    suspend fun getProjectByName(projectName: String): ProjectEntity?
}