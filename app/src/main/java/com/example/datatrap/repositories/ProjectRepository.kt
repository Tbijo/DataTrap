package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.datatrap.databaseio.dao.ProjectDao
import com.example.datatrap.models.Project
import com.example.datatrap.models.sync.ProjectSync

class ProjectRepository(private val projectDao: ProjectDao) {

    val projectList: LiveData<List<Project>> = projectDao.getProjects()

    suspend fun insertProject(project: Project) {
        projectDao.insertProject(project)
    }

    suspend fun updateProject(project: Project) {
        projectDao.updateProject(project)
    }

    suspend fun deleteProject(project: Project) {
        projectDao.deleteProject(project)
    }

    fun searchProjects(projectName: String): LiveData<List<Project>> {
        return projectDao.searchProjects(projectName)
    }

    suspend fun getProjectForSync(projectIds: List<Long>): List<Project> {
        return projectDao.getProjectForSync(projectIds)
    }

    suspend fun insertSyncProject(project: Project): Long {
        return projectDao.insertSyncProject(project)
    }

    suspend fun getProjectByName(projectName: String): Project? {
        return projectDao.getProjectByName(projectName)
    }
}