package com.example.datatrap.settings.data.protocol

import com.example.datatrap.settings.data.SettingsEntity
import com.example.datatrap.settings.data.SettingsEntityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.ZonedDateTime

class ProtocolRepository(
    private val protocolDao: ProtocolDao
): SettingsEntityRepository {

    fun getProtocolEntityList(): Flow<List<ProtocolEntity>> {
        return protocolDao.getProtocols()
    }

    override fun getSettingsEntityList(): Flow<List<SettingsEntity>> {
        return protocolDao.getProtocols()
    }

    // not to be used
    override fun getSettingsEntity(settingsEntityId: String): Flow<SettingsEntity> {
        return flow {
            ProtocolEntity(
                protocolName = "Empty",
                protDateTimeCreated = ZonedDateTime.now(),
                protDateTimeUpdated = ZonedDateTime.now(),
            )
        }
    }

    override suspend fun insertSettingsEntity(settingsEntity: SettingsEntity) {
        protocolDao.insertProtocol(settingsEntity.toProtocolEntity())
    }

    override suspend fun deleteSettingsEntity(settingsEntity: SettingsEntity) {
        protocolDao.deleteProtocol(settingsEntity.toProtocolEntity())
    }

}