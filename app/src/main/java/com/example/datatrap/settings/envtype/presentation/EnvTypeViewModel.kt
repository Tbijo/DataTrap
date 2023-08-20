package com.example.datatrap.settings.envtype.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.settings.envtype.data.EnvTypeEntity
import com.example.datatrap.settings.envtype.data.EnvTypeRepository
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
class EnvTypeViewModel @Inject constructor (
    private val envTypeRepository: EnvTypeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EnvTypeListUiState())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(
            isLoading = true
        ) }

        envTypeRepository.getEnvTypeEntityList().onEach { envList ->
            _state.update { it.copy(
                envTypeEntityList = envList,
                isLoading = false,
            ) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: EnvTypeListScreenEvent) {
        when(event) {
            is EnvTypeListScreenEvent.OnDeleteClick -> deleteEnvType(event.envTypeEntity)
            is EnvTypeListScreenEvent.OnInsertClick -> {
                insertEnvType(state.value.textNameValue)

                _state.update { it.copy(
                    envTypeEntity = null,
                    textNameValue = "",
                ) }
            }
            is EnvTypeListScreenEvent.OnItemClick -> {
                _state.update { it.copy(
                    envTypeEntity = event.envTypeEntity,
                ) }
            }

            is EnvTypeListScreenEvent.OnNameTextChanged -> {
                _state.update { it.copy(
                    textNameError = null,
                    textNameValue = event.text,
                ) }
            }
        }
    }

    private fun insertEnvType(name: String) {

        if (name.isEmpty()) {
            _state.update { it.copy(
                textNameError = "Empty field."
            ) }
            return
        }

        val envTypeEntity: EnvTypeEntity = if (state.value.envTypeEntity == null) {
            EnvTypeEntity(
                envTypeName = name,
                envTypeDateTimeCreated = ZonedDateTime.now(),
                envTypeDateTimeUpdated = null,
            )
        } else {
            EnvTypeEntity(
                envTypeId = state.value.envTypeEntity?.envTypeId ?: UUID.randomUUID().toString(),
                envTypeName = name,
                envTypeDateTimeCreated = state.value.envTypeEntity?.envTypeDateTimeCreated ?: ZonedDateTime.now(),
                envTypeDateTimeUpdated = ZonedDateTime.now(),
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            envTypeRepository.insertEnvType(envTypeEntity)
        }
    }

    private fun deleteEnvType(envTypeEntity: EnvTypeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            envTypeRepository.deleteEnvType(envTypeEntity)
        }
    }

}