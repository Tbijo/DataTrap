package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.ProjectLocalityDao
import com.example.datatrap.models.projectlocality.ProjectLocalityCrossRef
import com.example.datatrap.models.projectlocality.ProjectWithLocalities

class ProjectLocalityRepository(private val projectLocalityDao: ProjectLocalityDao) {

    suspend fun insertProjectLocality(projectLocalityCrossRef: ProjectLocalityCrossRef){
        projectLocalityDao.insertProjectLocalityCrossRef(projectLocalityCrossRef)
    }

    suspend fun deleteProjectLocality(projectLocalityCrossRef: ProjectLocalityCrossRef){
        projectLocalityDao.deleteProjectLocalityCrossRef(projectLocalityCrossRef)
    }

    fun getLocalitiesForProject(projectId: Long): LiveData<List<ProjectWithLocalities>>{
        return projectLocalityDao.getLocalitiesForProject(projectId)
    }
}