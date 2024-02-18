package com.example.datatrap.settings.data

import kotlinx.coroutines.flow.Flow

interface SettingsEntityRepository {

    fun getSettingsEntityList(): Flow<List<SettingsEntity>>

    fun getSettingsEntity(settingsEntityId: String): Flow<SettingsEntity>

    suspend fun insertSettingsEntity(settingsEntity: SettingsEntity)

    suspend fun deleteSettingsEntity(settingsEntity: SettingsEntity)

}