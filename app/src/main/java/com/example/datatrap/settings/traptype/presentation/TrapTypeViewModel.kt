package com.example.datatrap.settings.traptype.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.settings.traptype.data.TrapTypeEntity
import com.example.datatrap.settings.traptype.data.TrapTypeRepository
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
class TrapTypeViewModel @Inject constructor(
    private val trapTypeRepository: TrapTypeRepository
): ViewModel() {

    private val _state = MutableStateFlow(TrapTypeListUiState())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(
            isLoading = true,
        ) }

        trapTypeRepository.getTrapTypeEntityList().onEach { trapTypeList ->
            _state.update { it.copy(
                isLoading = false,
                trapTypeEntityList = trapTypeList,
            ) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: TrapTypeListScreenEvent) {
        when(event) {
            is TrapTypeListScreenEvent.OnDeleteClick -> deleteTrapType(event.trapTypeEntity)
            is TrapTypeListScreenEvent.OnInsertClick -> {
                insertTrapType(state.value.textNameValue)
                _state.update { it.copy(
                    trapTypeEntity = null,
                    textNameValue = "",
                ) }
            }
            is TrapTypeListScreenEvent.OnItemClick -> {
                _state.update { it.copy(
                    trapTypeEntity = event.trapTypeEntity,
                ) }
            }
            is TrapTypeListScreenEvent.OnNameTextChanged -> {
                _state.update { it.copy(
                    textNameError = null,
                    textNameValue = event.text
                ) }
            }
        }
    }

    private fun insertTrapType(name: String) {
        if (name.isNotEmpty()) {
            _state.update { it.copy(
                textNameError = "Empty field.",
            ) }
            return
        }

        val trapTypeEntity: TrapTypeEntity = if (state.value.trapTypeEntity == null) {
            TrapTypeEntity(
                trapTypeName = name,
                trapTypeDateTimeCreated = ZonedDateTime.now(),
                trapTypeDateTimeUpdated = null,
            )
        } else {
            TrapTypeEntity(
                trapTypeId = state.value.trapTypeEntity?.trapTypeId ?: UUID.randomUUID().toString(),
                trapTypeName = name,
                trapTypeDateTimeCreated = state.value.trapTypeEntity?.trapTypeDateTimeCreated ?: ZonedDateTime.now(),
                trapTypeDateTimeUpdated = ZonedDateTime.now(),
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            trapTypeRepository.insertTrapType(trapTypeEntity)
        }
    }

    private fun deleteTrapType(trapTypeEntity: TrapTypeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            trapTypeRepository.deleteTrapType(trapTypeEntity)
        }
    }
}