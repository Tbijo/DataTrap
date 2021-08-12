package com.example.datatrap.repositories

import com.example.datatrap.databaseio.dao.ProtocolDao
import com.example.datatrap.models.Protocol
import kotlinx.coroutines.flow.Flow

class ProtocolRepository(private val protocolDao: ProtocolDao) {

    suspend fun insertProtocol(protocol: Protocol){
        protocolDao.insertProtocol(protocol)
    }

    suspend fun updateProtocol(protocol: Protocol){
        protocolDao.updateProtocol(protocol)
    }

    suspend fun deleteProtocol(protocol: Protocol){
        protocolDao.deleteProtocol(protocol)
    }

    val protocolList: Flow<List<Protocol>> = protocolDao.getProtocols()

}