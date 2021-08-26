package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.ProtocolDao
import com.example.datatrap.models.Protocol

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

    val protocolList: LiveData<List<Protocol>> = protocolDao.getProtocols()

}