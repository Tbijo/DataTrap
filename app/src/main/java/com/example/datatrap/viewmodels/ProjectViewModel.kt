package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.Project
import com.example.datatrap.repositories.ProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProjectViewModel(application: Application): AndroidViewModel(application) {

    val projectList: Flow<List<Project>>
    private val projectRepository: ProjectRepository

    init {
        val projectDao = TrapDatabase.getDatabase(application).projectDao()
        projectRepository = ProjectRepository(projectDao)
        projectList = projectRepository.projectList
    }

    fun insertProject(project: Project){
        viewModelScope.launch(Dispatchers.IO){
            projectRepository.insertProject(project)
        }
    }

    fun updateProject(project: Project){
        viewModelScope.launch(Dispatchers.IO){
            projectRepository.updateProject(project)
        }
    }

    fun deleteProject(project: Project){
        viewModelScope.launch(Dispatchers.IO){
            projectRepository.deleteProject(project)
        }
    }

    fun searchProjects(projectName: String): Flow<List<Project>>{
        return projectRepository.searchProjects(projectName)
    }
}