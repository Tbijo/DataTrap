package com.example.datatrap.settings.data.methodtype

import com.example.datatrap.settings.data.SettingsEntity
import com.example.datatrap.settings.data.SettingsEntityRepository
import kotlinx.coroutines.flow.Flow

class MethodTypeRepository(
    private val methodTypeDao: MethodTypeDao
): SettingsEntityRepository {

    fun methodTypeEntityList(): Flow<List<MethodTypeEntity>> {
        return methodTypeDao.getMethodTypes()
    }

    fun getMethodType(methodTypeId: String): Flow<MethodTypeEntity> {
        return methodTypeDao.getMethodType(methodTypeId)
    }

    override fun getSettingsEntityList(): Flow<List<SettingsEntity>> {
        return methodTypeDao.getMethodTypes()
    }

    override fun getSettingsEntity(settingsEntityId: String): Flow<SettingsEntity> {
        return methodTypeDao.getMethodType(settingsEntityId)
    }

    override suspend fun insertSettingsEntity(settingsEntity: SettingsEntity) {
        methodTypeDao.insertMethodType(settingsEntity.toMethodTypeEntity())
    }

    override suspend fun deleteSettingsEntity(settingsEntity: SettingsEntity) {
        methodTypeDao.deleteMethodType(settingsEntity.toMethodTypeEntity())
    }
}