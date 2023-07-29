package com.example.datatrap.project.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.project.data.ProjectLocalityCrossRef
import com.example.datatrap.project.data.ProjectWithLocalities
import com.example.datatrap.project.data.ProjectLocalityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectLocalityViewModel @Inject constructor(
    private val projectLocalityRepository: ProjectLocalityRepository
): ViewModel() {

    fun insertProjectLocality(projectLocalityCrossRef: ProjectLocalityCrossRef) {
        viewModelScope.launch(Dispatchers.IO) {
            projectLocalityRepository.insertProjectLocality(projectLocalityCrossRef)
        }
    }

    fun deleteProjectLocality(projectLocalityCrossRef: ProjectLocalityCrossRef) {
        viewModelScope.launch(Dispatchers.IO) {
            projectLocalityRepository.deleteProjectLocality(projectLocalityCrossRef)
        }
    }

    fun getLocalitiesForProject(projectId: Long): LiveData<List<ProjectWithLocalities>> {
        return projectLocalityRepository.getLocalitiesForProject(projectId)
    }
}