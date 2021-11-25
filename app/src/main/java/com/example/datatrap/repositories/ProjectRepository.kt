package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.ProjectDao
import com.example.datatrap.models.Project

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
}