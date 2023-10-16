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

    suspend fun getProjectById(projectId: String): ProjectEntity {
        return projectDao.getProjectById(projectId)
    }

    fun searchProjects(projectName: String): Flow<List<ProjectEntity>> {
        return projectDao.searchProjects(projectName)
    }

    suspend fun getProjectByName(projectName: String): ProjectEntity? {
        return projectDao.getProjectByName(projectName)
    }
}