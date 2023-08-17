package com.example.datatrap.settings.protocol.data

import kotlinx.coroutines.flow.Flow

class ProtocolRepository(private val protocolDao: ProtocolDao) {

    val protocolEntityList: Flow<List<ProtocolEntity>> = protocolDao.getProtocols()

    suspend fun insertProtocol(protocolEntity: ProtocolEntity) {
        protocolDao.insertProtocol(protocolEntity)
    }

    suspend fun deleteProtocol(protocolEntity: ProtocolEntity) {
        protocolDao.deleteProtocol(protocolEntity)
    }

}