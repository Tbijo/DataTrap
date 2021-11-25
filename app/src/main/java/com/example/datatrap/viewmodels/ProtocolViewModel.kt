package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.Protocol
import com.example.datatrap.repositories.ProtocolRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProtocolViewModel(application: Application): AndroidViewModel(application) {

    val procolList: LiveData<List<Protocol>>
    private val protocolRepository: ProtocolRepository

    init {
        val protocolDao = TrapDatabase.getDatabase(application).protocolDao()
        protocolRepository = ProtocolRepository(protocolDao)
        procolList = protocolRepository.protocolList
    }

    fun insertProtocol(protocol: Protocol) {
        viewModelScope.launch(Dispatchers.IO) {
            protocolRepository.insertProtocol(protocol)
        }
    }

    fun updateProtocol(protocol: Protocol) {
        viewModelScope.launch(Dispatchers.IO) {
            protocolRepository.updateProtocol(protocol)
        }
    }

    fun deleteProtocol(protocol: Protocol) {
        viewModelScope.launch(Dispatchers.IO) {
            protocolRepository.deleteProtocol(protocol)
        }
    }
}