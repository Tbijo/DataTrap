package com.example.datatrap.settings.data.veg_type

import com.example.datatrap.settings.data.SettingsEntity
import com.example.datatrap.settings.data.SettingsEntityRepository
import kotlinx.coroutines.flow.Flow

class VegetTypeRepository(
    private val vegetTypeDao: VegetTypeDao
): SettingsEntityRepository {

    fun getVegetTypeEntityList(): Flow<List<VegetTypeEntity>> {
        return vegetTypeDao.getVegetTypes()
    }

    fun getVegetType(vegetTypeId: String): Flow<VegetTypeEntity> {
        return vegetTypeDao.getVegetType(vegetTypeId)
    }

    override fun getSettingsEntityList(): Flow<List<SettingsEntity>> {
        return vegetTypeDao.getVegetTypes()
    }

    override fun getSettingsEntity(settingsEntityId: String): Flow<SettingsEntity> {
        return vegetTypeDao.getVegetType(settingsEntityId)
    }

    override suspend fun insertSettingsEntity(settingsEntity: SettingsEntity) {
        vegetTypeDao.insertVegetType(settingsEntity.toVegetTypeEntity())
    }

    override suspend fun deleteSettingsEntity(settingsEntity: SettingsEntity) {
        vegetTypeDao.deleteVegetType(settingsEntity.toVegetTypeEntity())
    }
}