package com.example.datatrap.settings.data.methodtype

import com.example.datatrap.settings.data.SettingsEntity
import com.example.datatrap.settings.data.SettingsEntityRepository
import kotlinx.coroutines.flow.Flow

class MethodTypeRepository(
    private val methodTypeDao: MethodTypeDao
): SettingsEntityRepository {

    override fun getSettingsEntityList(): Flow<List<SettingsEntity>> {
        return methodTypeDao.getMethodTypes()
    }

    override suspend fun getSettingsEntity(settingsEntityId: String): SettingsEntity {
        return methodTypeDao.getMethodType(settingsEntityId)
    }

    override suspend fun insertSettingsEntity(settingsEntity: SettingsEntity) {
        methodTypeDao.insertMethodType(settingsEntity.toMethodTypeEntity())
    }

    override suspend fun deleteSettingsEntity(settingsEntity: SettingsEntity) {
        methodTypeDao.deleteMethodType(settingsEntity.toMethodTypeEntity())
    }
}