package com.example.datatrap.project.data

import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val projectDao: ProjectDao) {

    fun projectEntityList(): Flow<List<ProjectEntity>> {
        return projectDao.getProjects()
    }

    suspend fun insertProject(projectEntity: ProjectEntity) {
        projectDao.insertProject(projectEntity)
    }

    suspend fun deleteProject(projectEntity: ProjectEntity) {
        projectDao.deleteProject(projectEntity)
    }

    fun getProjectById(projectId: String): Flow<ProjectEntity> {
        return projectDao.getProjectById(projectId)
    }

    fun searchProjects(projectName: String): Flow<List<ProjectEntity>> {
        return projectDao.searchProjects(projectName)
    }

    suspend fun getProjectForSync(projectIds: List<Long>): List<ProjectEntity> {
        return projectDao.getProjectForSync(projectIds)
    }

    suspend fun insertSyncProject(projectEntity: ProjectEntity): Long {
        return projectDao.insertSyncProject(projectEntity)
    }

    suspend fun getProjectByName(projectName: String): ProjectEntity? {
        return projectDao.getProjectByName(projectName)
    }
}