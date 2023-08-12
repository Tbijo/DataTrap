package com.example.datatrap.settings.protocol.data

import androidx.lifecycle.LiveData

class ProtocolRepository(private val protocolDao: ProtocolDao) {

    val protocolEntityList: LiveData<List<ProtocolEntity>> = protocolDao.getProtocols()

    suspend fun insertProtocol(protocolEntity: ProtocolEntity) {
        protocolDao.insertProtocol(protocolEntity)
    }

    suspend fun updateProtocol(protocolEntity: ProtocolEntity) {
        protocolDao.updateProtocol(protocolEntity)
    }

    suspend fun deleteProtocol(protocolEntity: ProtocolEntity) {
        protocolDao.deleteProtocol(protocolEntity)
    }

}