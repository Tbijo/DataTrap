package com.example.datatrap.project.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.project.data.Project
import com.example.datatrap.project.data.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
): ViewModel() {

    val projectList: LiveData<List<Project>> = projectRepository.projectList

    fun insertProject(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.insertProject(project)
        }
    }

    fun updateProject(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.updateProject(project)
        }
    }

    fun deleteProject(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.deleteProject(project)
        }
    }

    fun searchProjects(projectName: String): LiveData<List<Project>> {
        return projectRepository.searchProjects(projectName)
    }
}