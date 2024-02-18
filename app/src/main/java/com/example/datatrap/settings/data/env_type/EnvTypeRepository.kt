package com.example.datatrap.settings.data.env_type

import com.example.datatrap.settings.data.SettingsEntity
import com.example.datatrap.settings.data.SettingsEntityRepository
import kotlinx.coroutines.flow.Flow

class EnvTypeRepository(
    private val envTypeDao: EnvTypeDao
): SettingsEntityRepository {
    fun getEnvTypeEntityList(): Flow<List<EnvTypeEntity>> {
        return envTypeDao.getEnvTypes()
    }

    fun getEnvType(envTypeId: String): Flow<EnvTypeEntity> {
        return envTypeDao.getEnvType(envTypeId)
    }

    override fun getSettingsEntityList(): Flow<List<SettingsEntity>> {
        return envTypeDao.getEnvTypes()
    }

    override fun getSettingsEntity(settingsEntityId: String): Flow<SettingsEntity> {
        return envTypeDao.getEnvType(settingsEntityId)
    }

    override suspend fun insertSettingsEntity(settingsEntity: SettingsEntity) {
        envTypeDao.insertEnvType(settingsEntity.toEnvTypeEntity())
    }

    override suspend fun deleteSettingsEntity(settingsEntity: SettingsEntity) {
        envTypeDao.deleteEnvType(settingsEntity.toEnvTypeEntity())
    }
}