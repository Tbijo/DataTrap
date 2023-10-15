package com.example.datatrap.core.data.project_locality

class ProjectLocalityRepository(private val projectLocalityDao: ProjectLocalityDao) {

    suspend fun insertProjectLocality(projectLocalityCrossRef: ProjectLocalityCrossRef) {
        projectLocalityDao.insertProjectLocalityCrossRef(projectLocalityCrossRef)
    }

    suspend fun deleteProjectLocality(projectLocalityCrossRef: ProjectLocalityCrossRef) {
        projectLocalityDao.deleteProjectLocalityCrossRef(projectLocalityCrossRef)
    }

    suspend fun existsProjectLocalityCrossRef(localityId: String, projectId: String): Boolean {
        return projectLocalityDao.existsProjectLocalityCrossRef(localityId, projectId)
    }
}