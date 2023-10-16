package com.example.datatrap.sync.data.local

import com.example.datatrap.locality.data.locality.LocalityEntity
import com.example.datatrap.mouse.data.MouseEntity
import com.example.datatrap.occasion.data.occasion.OccasionEntity
import com.example.datatrap.project.data.ProjectEntity
import com.example.datatrap.sync.data.remote.MouseImageSync
import com.example.datatrap.sync.data.remote.OccasionImageSync

class SyncRepository(
    private val syncDao: SyncDao,
) {
    // SYNC
    suspend fun getMiceForSync(lastSync: Long): List<MouseEntity> {
        return syncDao.getMiceForSync(lastSync)
    }

    suspend fun insertSynctMouse(mouseEntity: MouseEntity) {
        syncDao.insertSynctMouse(mouseEntity)
    }

    suspend fun getSyncMouse(occasionID: String, mouseCaught: Long): MouseEntity? {
        return syncDao.getSyncMouse(occasionID, mouseCaught)
    }

    // occasion sync
    // SYNC
    suspend fun getOccasionForSync(occasionId: List<String>): List<OccasionEntity> {
        return syncDao.getOccasionForSync(occasionId)
    }

    suspend fun getSyncOccasion(sessionID: String, occasionStart: Long): OccasionEntity? {
        return syncDao.getSyncOccasion(sessionID, occasionStart)
    }

    // image
    suspend fun getOccasionImages(unixTime: Long): List<OccasionImageSync> {
        return syncDao.getOccasionImages(unixTime)
    }

    suspend fun getMouseImages(unixTime: Long): List<MouseImageSync> {
        return syncDao.getMouseImages(unixTime)
    }

//    suspend fun getSpecieImages(unixTime: Long): List<SpecieImageSync> {
//        return syncDao.getSpecieImages(unixTime)
//    }

    suspend fun insertSyncLocality(localityEntity: LocalityEntity) {
        return syncDao.insertSyncLocality(localityEntity)
    }

    suspend fun insertSyncProject(projectEntity: ProjectEntity) {
        syncDao.insertSyncProject(projectEntity)
    }

    suspend fun getProjectForSync(projectIds: List<Long>): List<ProjectEntity> {
        return syncDao.getProjectForSync(projectIds)
    }
}