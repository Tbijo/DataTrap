package com.example.datatrap.locality.presentation.locality_add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.core.util.Resource
import com.example.datatrap.locality.data.locality.LocalityEntity
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.locality.domain.LocationClient
import com.example.datatrap.locality.navigation.LocalityScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LocalityViewModel @Inject constructor(
    private val localityRepository: LocalityRepository,
    private val locationClient: LocationClient,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _state = MutableStateFlow(LocalityUiState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        _state.update { it.copy(
            isLoading = true,
        ) }
        savedStateHandle.getStateFlow<String?>(LocalityScreens.LocalityScreen.localityIdKey, null).onEach { locId ->
            locId?.let {
                localityRepository.getLocality(locId).onEach { localEntity ->
                    _state.update { it.copy(
                        isLoading = false,
                        localityEntity = localEntity,
                        localityName = localEntity.localityName,
                        numSessions = localEntity.numSessions.toString(),
                        note = localEntity.note ?: "",
                        latitudeA = if (localEntity.xA == null) "" else localEntity.xA.toString(),
                        longitudeA = if (localEntity.yA == null) "" else localEntity.yA.toString(),
                        latitudeB = if (localEntity.xB == null) "" else localEntity.xB.toString(),
                        longitudeB = if (localEntity.yB == null) "" else localEntity.yB.toString(),
                    ) }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: LocalityScreenEvent) {
        when(event) {
            LocalityScreenEvent.OnInsertClick -> insertLocality()
            is LocalityScreenEvent.OnLocalityNameChange -> {
                _state.update { it.copy(
                    localityName = event.text,
                    localityNameError = null,
                ) }
            }
            is LocalityScreenEvent.OnButtonCoorAClick -> getLocationA()

            is LocalityScreenEvent.OnButtonCoorBClick -> getLocationB()

            is LocalityScreenEvent.OnLatitudeAChange -> {
                _state.update { it.copy(
                    latitudeA = event.text,
                ) }
            }
            is LocalityScreenEvent.OnLongitudeAChange -> {
                _state.update { it.copy(
                    longitudeA = event.text,
                ) }
            }
            is LocalityScreenEvent.OnLatitudeBChange -> {
                _state.update { it.copy(
                    latitudeB = event.text,
                ) }
            }
            is LocalityScreenEvent.OnLongitudeBChange -> {
                _state.update { it.copy(
                    longitudeB = event.text,
                ) }
            }
            is LocalityScreenEvent.OnNoteChange -> {
                _state.update { it.copy(
                    note = event.text
                ) }
            }
            is LocalityScreenEvent.OnNumSessionsChange -> {
                _state.update { it.copy(
                    numSessions = event.text,
                ) }
            }
        }
    }

    private fun insertLocality() {
        val localityName = state.value.localityName.ifEmpty {
            _state.update { it.copy(
                localityNameError = "Locality must have a name"
            ) }
            return
        }
        val numSessions = Integer.parseInt(state.value.numSessions.ifEmpty { "0" })
        val latitudeA = state.value.latitudeA.ifEmpty { null }
        val longitudeA = state.value.longitudeA.ifEmpty { null }
        val latitudeB = state.value.latitudeB.ifEmpty { null }
        val longitudeB = state.value.longitudeB.ifEmpty { null }
        val note = state.value.note.ifEmpty { null }

        val localityEntity = if (state.value.localityEntity == null) {
            LocalityEntity(
                localityId = UUID.randomUUID().toString(),
                localityName = localityName,
                numSessions = numSessions,
                xA = latitudeA?.toFloat(),
                yA = longitudeA?.toFloat(),
                xB = latitudeB?.toFloat(),
                yB = longitudeB?.toFloat(),
                localityDateTimeCreated = ZonedDateTime.now(),
                localityDateTimeUpdated = null,
                note = note,
            )
        } else {
            LocalityEntity(
                localityId = state.value.localityEntity?.localityId ?: UUID.randomUUID().toString(),
                localityName = localityName,
                numSessions = numSessions,
                xA = latitudeA?.toFloat(),
                yA = longitudeA?.toFloat(),
                xB = latitudeB?.toFloat(),
                yB = longitudeB?.toFloat(),
                localityDateTimeCreated = state.value.localityEntity?.localityDateTimeUpdated ?: ZonedDateTime.now(),
                localityDateTimeUpdated = ZonedDateTime.now(),
                note = note,
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            localityRepository.insertLocality(localityEntity)
            _eventFlow.emit(UiEvent.NavigateBack)
        }
    }

    private fun getLocationA() {
        locationClient.getLocation().onEach { result ->
            when(result) {
                is Resource.Error -> {
                    _state.update { it.copy(
                        error = result.throwable?.message.toString()
                    ) }
                }
                is Resource.Success -> {
                    _state.update { it.copy(
                        latitudeA = result.data?.latitude.toString(),
                        longitudeA = result.data?.longitude.toString(),
                    ) }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getLocationB() {
        locationClient.getLocation().onEach { result ->
            when(result) {
                is Resource.Error -> {
                    _state.update { it.copy(
                        error = result.throwable?.message.toString()
                    ) }
                }
                is Resource.Success -> {
                    _state.update { it.copy(
                        latitudeB = result.data?.latitude.toString(),
                        longitudeB = result.data?.longitude.toString(),
                    ) }
                }
            }
        }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        locationClient.cancelLocationProvider()
    }
}