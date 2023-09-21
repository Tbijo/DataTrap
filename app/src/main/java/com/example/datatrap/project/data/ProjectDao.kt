package com.example.datatrap.project.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(projectEntity: ProjectEntity)

    @Delete
    suspend fun deleteProject(projectEntity: ProjectEntity)

    @Query("SELECT * FROM ProjectEntity")
    fun getProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM ProjectEntity WHERE projectId = :projectId")
    suspend fun getProjectById(projectId: String): ProjectEntity

    @Query("SELECT * FROM ProjectEntity WHERE ProjectName LIKE :nameProject")
    fun searchProjects(nameProject: String): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM ProjectEntity WHERE projectId IN (:projectIds)")
    suspend fun getProjectForSync(projectIds: List<Long>): List<ProjectEntity>

    // Sync
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncProject(projectEntity: ProjectEntity): Long

    @Query("SELECT * FROM ProjectEntity WHERE projectName = :projectName")
    suspend fun getProjectByName(projectName: String): ProjectEntity?
}