package com.example.datatrap.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.settings.SettingsScreenNames
import com.example.datatrap.settings.data.SettingsEntity
import com.example.datatrap.settings.data.SettingsEntityRepository
import com.example.datatrap.settings.data.env_type.EnvTypeRepository
import com.example.datatrap.settings.data.method.MethodRepository
import com.example.datatrap.settings.data.methodtype.MethodTypeRepository
import com.example.datatrap.settings.data.protocol.ProtocolRepository
import com.example.datatrap.settings.data.traptype.TrapTypeRepository
import com.example.datatrap.settings.data.veg_type.VegetTypeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.util.UUID

class SettingsEntityViewModel(
    private val envTypeRepository: EnvTypeRepository,
    private val methodRepository: MethodRepository,
    private val methodTypeRepository: MethodTypeRepository,
    private val protocolRepository: ProtocolRepository,
    private val trapTypeRepository: TrapTypeRepository,
    private val vegetTypeRepository: VegetTypeRepository,
    private val settingsEntity: SettingsScreenNames,
): ViewModel() {

    private val _state = MutableStateFlow(SettingsEntityUiState())
    val state = _state.asStateFlow()

    private lateinit var settingsEntityRepository: SettingsEntityRepository

    init {
        setRepository()

        setDefaultLabels()

        settingsEntityRepository.getSettingsEntityList().onEach { entList ->
            _state.update { it.copy(
                entityList = entList,
                isLoading = false,
            ) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: SettingsEntityEvent) {
        when(event) {
            is SettingsEntityEvent.OnDeleteClick -> deleteSettingsEntity(event.settingsEntity)

            is SettingsEntityEvent.OnInsertClick -> {
                insertSettingsEntity(state.value.textNameValue)

                _state.update { it.copy(
                    entity = null,
                    textNameValue = "",
                ) }
            }

            is SettingsEntityEvent.OnItemClick -> {
                _state.update { it.copy(
                    entity = event.settingsEntity,
                ) }
            }

            is SettingsEntityEvent.OnNameTextChanged -> {
                _state.update { it.copy(
                    textNameError = null,
                    textNameValue = event.text,
                ) }
            }
        }
    }

    private fun insertSettingsEntity(name: String) {

        if (name.isEmpty()) {
            _state.update { it.copy(
                textNameError = "Empty field."
            ) }
            return
        }

        val settingsEntity: SettingsEntity = if (state.value.entity == null) {
            SettingsEntity(
                entityName = name,
                entityDateTimeCreated = ZonedDateTime.now(),
                entityDateTimeUpdated = null,
            )
        } else {
            SettingsEntity(
                entityId = state.value.entity?.entityId ?: UUID.randomUUID().toString(),
                entityName = name,
                entityDateTimeCreated = state.value.entity?.entityDateTimeCreated ?: ZonedDateTime.now(),
                entityDateTimeUpdated = ZonedDateTime.now(),
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            settingsEntityRepository.insertSettingsEntity(settingsEntity)
        }
    }

    private fun deleteSettingsEntity(settingsEntity: SettingsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsEntityRepository.deleteSettingsEntity(settingsEntity)
        }
    }

    private fun setRepository() {
        settingsEntityRepository = when(settingsEntity) {
            SettingsScreenNames.ENVIRONMENT -> envTypeRepository
            SettingsScreenNames.METHOD -> methodRepository
            SettingsScreenNames.METHODTYPE -> methodTypeRepository
            SettingsScreenNames.PROTOCOL -> protocolRepository
            SettingsScreenNames.TRAPTYPE -> trapTypeRepository
            SettingsScreenNames.VEGETTYPE -> vegetTypeRepository
            else -> {
                // This should never happen
                envTypeRepository
            }
        }
    }

    private fun setDefaultLabels() {
        _state.update { it.copy(
            title = setTitle(),
            iconDescription = setIconDesc(),
            placeholder = setPlaceHolderAndLabel(),
            label = setPlaceHolderAndLabel(),
        ) }
    }

    private fun setTitle(): String {
        return when(settingsEntity) {
            SettingsScreenNames.ENVIRONMENT -> "EnvType List"
            SettingsScreenNames.METHOD -> "Method List"
            SettingsScreenNames.METHODTYPE -> "Method Type List"
            SettingsScreenNames.PROTOCOL -> "Protocol List"
            SettingsScreenNames.TRAPTYPE -> "Trap Type List"
            SettingsScreenNames.VEGETTYPE -> "Veget Type List"
            else -> "Error List"
        }
    }
    private fun setIconDesc(): String {
        return when(settingsEntity) {
            SettingsScreenNames.ENVIRONMENT -> "Add EnvType"
            SettingsScreenNames.METHOD -> "Add Method"
            SettingsScreenNames.METHODTYPE -> "Add Method Type"
            SettingsScreenNames.PROTOCOL -> "Add Protocol"
            SettingsScreenNames.TRAPTYPE -> "Add Trap Type"
            SettingsScreenNames.VEGETTYPE -> "Add Vegetation Type"
            else -> "Error"
        }
    }
    private fun setPlaceHolderAndLabel(): String {
        return when(settingsEntity) {
            SettingsScreenNames.ENVIRONMENT -> "Environment Type"
            SettingsScreenNames.METHOD -> "Method"
            SettingsScreenNames.METHODTYPE -> "Method Type"
            SettingsScreenNames.PROTOCOL -> "Protocol"
            SettingsScreenNames.TRAPTYPE -> "Trap Type"
            SettingsScreenNames.VEGETTYPE -> "Vegetation Type"
            else -> "Error"
        }
    }
}