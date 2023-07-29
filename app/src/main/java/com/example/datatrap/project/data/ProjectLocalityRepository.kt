package com.example.datatrap.project.data

import androidx.lifecycle.LiveData

class ProjectLocalityRepository(private val projectLocalityDao: ProjectLocalityDao) {

    suspend fun insertProjectLocality(projectLocalityCrossRef: ProjectLocalityCrossRef) {
        projectLocalityDao.insertProjectLocalityCrossRef(projectLocalityCrossRef)
    }

    suspend fun deleteProjectLocality(projectLocalityCrossRef: ProjectLocalityCrossRef) {
        projectLocalityDao.deleteProjectLocalityCrossRef(projectLocalityCrossRef)
    }

    fun getLocalitiesForProject(projectId: Long): LiveData<List<ProjectWithLocalities>> {
        return projectLocalityDao.getLocalitiesForProject(projectId)
    }
}