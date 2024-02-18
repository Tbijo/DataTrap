package com.example.datatrap.settings.data.method

import com.example.datatrap.settings.data.SettingsEntity
import com.example.datatrap.settings.data.SettingsEntityRepository
import kotlinx.coroutines.flow.Flow

class MethodRepository(
    private val methodDao: MethodDao
): SettingsEntityRepository {
    fun methodEntityList(): Flow<List<MethodEntity>> {
        return methodDao.getMethods()
    }

    fun getMethod(methodId: String): Flow<MethodEntity> {
        return methodDao.getMethod(methodId)
    }

    override fun getSettingsEntityList(): Flow<List<SettingsEntity>> {
        return methodDao.getMethods()
    }

    override fun getSettingsEntity(settingsEntityId: String): Flow<SettingsEntity> {
        return methodDao.getMethod(settingsEntityId)
    }

    override suspend fun insertSettingsEntity(settingsEntity: SettingsEntity) {
        methodDao.insertMethod(settingsEntity.toMethodEntity())
    }

    override suspend fun deleteSettingsEntity(settingsEntity: SettingsEntity) {
        methodDao.deleteMethod(settingsEntity.toMethodEntity())
    }
}