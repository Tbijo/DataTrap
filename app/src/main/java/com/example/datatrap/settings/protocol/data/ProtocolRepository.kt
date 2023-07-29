package com.example.datatrap.settings.protocol.data

import androidx.lifecycle.LiveData

class ProtocolRepository(private val protocolDao: ProtocolDao) {

    val protocolList: LiveData<List<Protocol>> = protocolDao.getProtocols()

    suspend fun insertProtocol(protocol: Protocol) {
        protocolDao.insertProtocol(protocol)
    }

    suspend fun updateProtocol(protocol: Protocol) {
        protocolDao.updateProtocol(protocol)
    }

    suspend fun deleteProtocol(protocol: Protocol) {
        protocolDao.deleteProtocol(protocol)
    }

}