package com.example.datatrap.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.models.Protocol
import com.example.datatrap.repositories.ProtocolRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProtocolViewModel @Inject constructor(
    private val protocolRepository: ProtocolRepository
): ViewModel() {

    val procolList: LiveData<List<Protocol>> = protocolRepository.protocolList

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