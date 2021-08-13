package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.ProjectLocalityDao
import com.example.datatrap.models.relations.ProjectLocalityCrossRef
import com.example.datatrap.models.relations.ProjectWithLocalities
import kotlinx.coroutines.flow.Flow

class ProjectLocalityRepository(private val projectLocalityDao: ProjectLocalityDao) {

    suspend fun insertProjectLocality(projectLocalityCrossRef: ProjectLocalityCrossRef){
        projectLocalityDao.insertProjectLocalityCrossRef(projectLocalityCrossRef)
    }

    suspend fun deleteProjectLocality(projectLocalityCrossRef: ProjectLocalityCrossRef){
        projectLocalityDao.deleteProjectLocalityCrossRef(projectLocalityCrossRef)
    }

    fun getLocalitiesForProject(projectName: String): LiveData<List<ProjectWithLocalities>>{
        return projectLocalityDao.getLocalitiesForProject(projectName)
    }
}