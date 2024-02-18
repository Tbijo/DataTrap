package com.example.datatrap.settings.data.traptype

import com.example.datatrap.settings.data.SettingsEntity
import com.example.datatrap.settings.data.SettingsEntityRepository
import kotlinx.coroutines.flow.Flow

class TrapTypeRepository(
    private val trapTypeDao: TrapTypeDao
): SettingsEntityRepository {

    fun getTrapTypeEntityList(): Flow<List<TrapTypeEntity>> {
        return trapTypeDao.getTrapTypes()
    }

    fun getTrapType(trapTypeId: String): Flow<TrapTypeEntity> {
        return trapTypeDao.getTrapType(trapTypeId)
    }

    override fun getSettingsEntityList(): Flow<List<SettingsEntity>> {
        return trapTypeDao.getTrapTypes()
    }

    override fun getSettingsEntity(settingsEntityId: String): Flow<SettingsEntity> {
        return trapTypeDao.getTrapType(settingsEntityId)
    }

    override suspend fun insertSettingsEntity(settingsEntity: SettingsEntity) {
        trapTypeDao.insertTrapType(settingsEntity.toTrapTypeEntity())
    }

    override suspend fun deleteSettingsEntity(settingsEntity: SettingsEntity) {
        trapTypeDao.deleteTrapType(settingsEntity.toTrapTypeEntity())
    }
}