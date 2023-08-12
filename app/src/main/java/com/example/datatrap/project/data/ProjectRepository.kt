package com.example.datatrap.project.data

import androidx.lifecycle.LiveData

class ProjectRepository(private val projectDao: ProjectDao) {

    val projectEntityList: LiveData<List<ProjectEntity>> = projectDao.getProjects()

    suspend fun insertProject(projectEntity: ProjectEntity) {
        projectDao.insertProject(projectEntity)
    }

    suspend fun updateProject(projectEntity: ProjectEntity) {
        projectDao.updateProject(projectEntity)
    }

    suspend fun deleteProject(projectEntity: ProjectEntity) {
        projectDao.deleteProject(projectEntity)
    }

    fun searchProjects(projectName: String): LiveData<List<ProjectEntity>> {
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