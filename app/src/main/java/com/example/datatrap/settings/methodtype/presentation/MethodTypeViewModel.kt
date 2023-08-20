package com.example.datatrap.settings.methodtype.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.settings.methodtype.data.MethodTypeEntity
import com.example.datatrap.settings.methodtype.data.MethodTypeRepository
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
class MethodTypeViewModel @Inject constructor(
    private val methodTypeRepository: MethodTypeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MethodTypeListUiState())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(
            isLoading = true,
        ) }

        methodTypeRepository.methodTypeEntityList().onEach { methodTypeList ->
            _state.update { it.copy(
                isLoading = false,
                methodTypeEntityList = methodTypeList,
            ) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: MethodTypeListScreenEvent) {
        when(event) {
            is MethodTypeListScreenEvent.OnDeleteClick -> deleteMethodType(event.methodTypeEntity)
            is MethodTypeListScreenEvent.OnInsertClick -> {
                insertMethodType(state.value.textNameValue)
                _state.update { it.copy(
                    methodTypeEntity = null,
                    textNameValue = "",
                ) }
            }
            is MethodTypeListScreenEvent.OnItemClick -> {
                _state.update { it.copy(
                    methodTypeEntity = event.methodTypeEntity
                ) }
            }
            is MethodTypeListScreenEvent.OnNameTextChanged -> {
                _state.update { it.copy(
                    textNameError = null,
                    textNameValue = event.text,
                ) }
            }
        }
    }

    private fun insertMethodType(name: String) {
        if (name.isNotEmpty()) {
            _state.update { it.copy(
                textNameError = "Empty field.",
            ) }
            return
        }

        val methodTypeEntity: MethodTypeEntity = if (state.value.methodTypeEntity == null) {
            MethodTypeEntity(
                methodTypeName = name,
                methTypeDateTimeCreated = ZonedDateTime.now(),
                methTypeDateTimeUpdated = null,
            )
        } else {
            MethodTypeEntity(
                methodTypeId = state.value.methodTypeEntity?.methodTypeId ?: UUID.randomUUID().toString(),
                methodTypeName = name,
                methTypeDateTimeCreated = state.value.methodTypeEntity?.methTypeDateTimeCreated ?: ZonedDateTime.now(),
                methTypeDateTimeUpdated = ZonedDateTime.now(),
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            methodTypeRepository.insertMethodType(methodTypeEntity)
        }
    }

    private fun deleteMethodType(methodTypeEntity: MethodTypeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            methodTypeRepository.deleteMethodType(methodTypeEntity)
        }
    }
}