package com.example.datatrap.settings.method.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.settings.method.data.MethodEntity
import com.example.datatrap.settings.method.data.MethodRepository
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
class MethodViewModel @Inject constructor(
    private val methodRepository: MethodRepository
): ViewModel() {

    private val _state = MutableStateFlow(MethodListUiState())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(
            isLoading = true
        ) }

        methodRepository.methodEntityList().onEach { methodList ->
            _state.update { it.copy(
                methodEntityList = methodList,
                isLoading = false,
            ) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: MethodListScreenEvent) {
        when(event) {
            is MethodListScreenEvent.OnDeleteClick -> deleteMethod(event.methodEntity)
            is MethodListScreenEvent.OnInsertClick -> {
                insertMethod(state.value.textNameValue)

                _state.update { it.copy(
                    methodEntity = null,
                    textNameValue = "",
                ) }
            }
            is MethodListScreenEvent.OnItemClick -> {
                _state.update { it.copy(
                    methodEntity = event.methodEntity,
                ) }
            }
            is MethodListScreenEvent.OnNameTextChanged -> {
                _state.update { it.copy(
                    textNameError = null,
                    textNameValue = event.text
                ) }
            }
        }
    }

    private fun insertMethod(name: String) {

        if (name.isEmpty()) {
            _state.update { it.copy(
                textNameError = "Empty field.",
            ) }
            return
        }

        val methodEntity: MethodEntity = if (state.value.methodEntity == null) {
            MethodEntity(
                methodName = name,
                methodDateTimeCreated = ZonedDateTime.now(),
                methodDateTimeUpdated = null,
            )
        } else {
            MethodEntity(
                methodId = state.value.methodEntity?.methodId ?: UUID.randomUUID().toString(),
                methodName = name,
                methodDateTimeCreated = state.value.methodEntity?.methodDateTimeCreated ?: ZonedDateTime.now(),
                methodDateTimeUpdated = ZonedDateTime.now(),
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            methodRepository.insertMethod(methodEntity)
        }
    }

    private fun deleteMethod(methodEntity: MethodEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            methodRepository.deleteMethod(methodEntity)
        }
    }

}