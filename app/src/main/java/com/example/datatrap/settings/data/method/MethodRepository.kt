package com.example.datatrap.settings.data.method

import com.example.datatrap.settings.data.SettingsEntity
import com.example.datatrap.settings.data.SettingsEntityRepository
import kotlinx.coroutines.flow.Flow

class MethodRepository(
    private val methodDao: MethodDao
): SettingsEntityRepository {

    override fun getSettingsEntityList(): Flow<List<SettingsEntity>> {
        return methodDao.getMethods()
    }

    override suspend fun getSettingsEntity(settingsEntityId: String): SettingsEntity {
        return methodDao.getMethod(settingsEntityId)
    }

    override suspend fun insertSettingsEntity(settingsEntity: SettingsEntity) {
        methodDao.insertMethod(settingsEntity.toMethodEntity())
    }

    override suspend fun deleteSettingsEntity(settingsEntity: SettingsEntity) {
        methodDao.deleteMethod(settingsEntity.toMethodEntity())
    }
}