package com.example.datatrap.settings.data.env_type

import com.example.datatrap.settings.data.SettingsEntity
import com.example.datatrap.settings.data.SettingsEntityRepository
import kotlinx.coroutines.flow.Flow

class EnvTypeRepository(
    private val envTypeDao: EnvTypeDao
): SettingsEntityRepository {

    override fun getSettingsEntityList(): Flow<List<SettingsEntity>> {
        return envTypeDao.getEnvTypes()
    }

    override suspend fun getSettingsEntity(settingsEntityId: String): SettingsEntity {
        return envTypeDao.getEnvType(settingsEntityId)
    }

    override suspend fun insertSettingsEntity(settingsEntity: SettingsEntity) {
        envTypeDao.insertEnvType(settingsEntity.toEnvTypeEntity())
    }

    override suspend fun deleteSettingsEntity(settingsEntity: SettingsEntity) {
        envTypeDao.deleteEnvType(settingsEntity.toEnvTypeEntity())
    }
}