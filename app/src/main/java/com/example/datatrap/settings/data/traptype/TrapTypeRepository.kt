package com.example.datatrap.settings.data.traptype

import com.example.datatrap.settings.data.SettingsEntity
import com.example.datatrap.settings.data.SettingsEntityRepository
import kotlinx.coroutines.flow.Flow

class TrapTypeRepository(
    private val trapTypeDao: TrapTypeDao
): SettingsEntityRepository {

    override fun getSettingsEntityList(): Flow<List<SettingsEntity>> {
        return trapTypeDao.getTrapTypes()
    }

    override suspend fun getSettingsEntity(settingsEntityId: String): SettingsEntity {
        return trapTypeDao.getTrapType(settingsEntityId)
    }

    override suspend fun insertSettingsEntity(settingsEntity: SettingsEntity) {
        trapTypeDao.insertTrapType(settingsEntity.toTrapTypeEntity())
    }

    override suspend fun deleteSettingsEntity(settingsEntity: SettingsEntity) {
        trapTypeDao.deleteTrapType(settingsEntity.toTrapTypeEntity())
    }
}