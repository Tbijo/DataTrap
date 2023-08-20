package com.example.datatrap.settings.vegettype.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.settings.vegettype.data.VegetTypeEntity
import com.example.datatrap.settings.vegettype.data.VegetTypeRepository
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
class VegetTypeViewModel @Inject constructor(
    private val vegetTypeRepository: VegetTypeRepository
): ViewModel() {

    private val _state = MutableStateFlow(VegetTypeListUiState())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(
            isLoading = true,
        ) }

        vegetTypeRepository.getVegetTypeEntityList().onEach { vegetTypeList ->
            _state.update { it.copy(
                isLoading = false,
                vegetTypeEntityList = vegetTypeList,
            ) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: VegetTypeListScreenEvent) {
        when(event) {
            is VegetTypeListScreenEvent.OnDeleteClick -> deleteVegType(event.vegetTypeEntity)
            is VegetTypeListScreenEvent.OnInsertClick -> {
                insertVegType(state.value.textNameValue)
                _state.update { it.copy(
                    vegetTypeEntity = null,
                    textNameValue = "",
                ) }
            }
            is VegetTypeListScreenEvent.OnItemClick -> {
                _state.update { it.copy(
                    vegetTypeEntity = event.vegetTypeEntity,
                ) }
            }
            is VegetTypeListScreenEvent.OnNameTextChanged -> {
                _state.update { it.copy(
                    textNameError = null,
                    textNameValue = event.text
                ) }
            }
        }
    }

    private fun insertVegType(name: String) {
        if (name.isNotEmpty()) {
            _state.update { it.copy(
                textNameError = "Empty field.",
            ) }
            return
        }

        val vegetTypeEntity: VegetTypeEntity = if (state.value.vegetTypeEntity == null) {
            VegetTypeEntity(
                vegetTypeName = name,
                vegTypeDateTimeCreated = ZonedDateTime.now(),
                vegTypeDateTimeUpdated = null,
            )
        } else {
            VegetTypeEntity(
                vegetTypeId = state.value.vegetTypeEntity?.vegetTypeId ?: UUID.randomUUID().toString(),
                vegetTypeName = name,
                vegTypeDateTimeCreated = state.value.vegetTypeEntity?.vegTypeDateTimeCreated ?: ZonedDateTime.now(),
                vegTypeDateTimeUpdated = ZonedDateTime.now(),
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            vegetTypeRepository.insertVegetType(vegetTypeEntity)
        }
    }

    private fun deleteVegType(vegType: VegetTypeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            vegetTypeRepository.deleteVegetType(vegType)
        }
    }
}