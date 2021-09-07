package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.relations.LocalityWithProjects
import com.example.datatrap.models.relations.ProjectLocalityCrossRef
import com.example.datatrap.models.relations.ProjectWithLocalities
import com.example.datatrap.repositories.ProjectLocalityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProjectLocalityViewModel(application: Application): AndroidViewModel(application) {

    private val projectLocalityRepository: ProjectLocalityRepository

    init {
        val projectLocalityDao = TrapDatabase.getDatabase(application).projectLocalityDao()
        projectLocalityRepository = ProjectLocalityRepository(projectLocalityDao)
    }

    fun insertProjectLocality(projectLocalityCrossRef: ProjectLocalityCrossRef){
        viewModelScope.launch(Dispatchers.IO) {
            projectLocalityRepository.insertProjectLocality(projectLocalityCrossRef)
        }
    }

    fun deleteProjectLocality(projectLocalityCrossRef: ProjectLocalityCrossRef){
        viewModelScope.launch(Dispatchers.IO) {
            projectLocalityRepository.deleteProjectLocality(projectLocalityCrossRef)
        }
    }

    fun getLocalitiesForProject(projectId: Long): LiveData<List<ProjectWithLocalities>> {
        return projectLocalityRepository.getLocalitiesForProject(projectId)
    }

    fun getProjectsForLocality(localityId: Long): LiveData<List<LocalityWithProjects>>{
        return projectLocalityRepository.getProjectsForLocality(localityId)
    }
}