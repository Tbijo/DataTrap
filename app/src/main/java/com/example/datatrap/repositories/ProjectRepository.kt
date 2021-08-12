package com.example.datatrap.repositories

import com.example.datatrap.databaseio.dao.ProjectDao
import com.example.datatrap.models.Project
import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val projectDao: ProjectDao) {

    suspend fun insertProject(project: Project){
        projectDao.insertProject(project)
    }

    suspend fun updateProject(project: Project){
        projectDao.updateProject(project)
    }

    suspend fun deleteProject(project: Project){
        projectDao.deleteProject(project)
    }

    val projectList: Flow<List<Project>> = projectDao.getProjects()

    fun searchProjects(projectName: String): Flow<List<Project>>{
        return projectDao.searchProjects(projectName)
    }
}