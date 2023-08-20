package com.example.datatrap.settings.protocol.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.settings.protocol.data.ProtocolEntity
import com.example.datatrap.settings.protocol.data.ProtocolRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProtocolViewModel @Inject constructor(
    private val protocolRepository: ProtocolRepository
): ViewModel() {

    private val _state = MutableStateFlow(ProtocolListUiState())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(
            isLoading = true
        ) }

        protocolRepository.getProtocolEntityList().onEach { protocolList ->
            _state.update { it.copy(
                isLoading = false,
                protocolEntityList = protocolList,
            ) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: ProtocolListScreenEvent) {
        when(event) {
            is ProtocolListScreenEvent.OnDeleteClick -> deleteProtocol(event.protocolEntity)
            is ProtocolListScreenEvent.OnInsertClick -> {
                insertProtocol(state.value.textNameValue)
                _state.update { it.copy(
                    protocolEntity = null,
                    textNameValue = "",
                ) }
            }
            is ProtocolListScreenEvent.OnItemClick -> {
                _state.update { it.copy(
                    protocolEntity = event.protocolEntity,
                ) }
            }
            is ProtocolListScreenEvent.OnNameTextChanged -> {
                _state.update { it.copy(
                    textNameError = null,
                    textNameValue = event.text,
                ) }
            }
        }
    }

    private fun insertProtocol(name: String) {
        if (name.isNotEmpty()) {
            _state.update { it.copy(
                textNameError = "Empty field.",
            ) }
            return
        }

        val protocolEntity: ProtocolEntity = if (state.value.protocolEntity == null) {
            ProtocolEntity(
                protocolName = name,
                protDateTimeCreated = ZonedDateTime.now(),
                protDateTimeUpdated = null,
            )
        } else {
            ProtocolEntity(
                protocolId = state.value.protocolEntity?.protocolId ?: UUID.randomUUID().toString(),
                protocolName = name,
                protDateTimeCreated = state.value.protocolEntity?.protDateTimeCreated ?: ZonedDateTime.now(),
                protDateTimeUpdated = ZonedDateTime.now(),
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            protocolRepository.insertProtocol(protocolEntity)
        }
    }

    private fun deleteProtocol(protocolEntity: ProtocolEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            protocolRepository.deleteProtocol(protocolEntity)
        }
    }
}