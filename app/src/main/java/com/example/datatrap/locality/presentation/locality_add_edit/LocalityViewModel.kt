package com.example.datatrap.locality.presentation.locality_add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.core.util.Resource
import com.example.datatrap.locality.data.locality.LocalityEntity
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.locality.domain.LocationClient
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

class LocalityViewModel(
    private val localityRepository: LocalityRepository,
    private val locationClient: LocationClient,
    private val localityId: String?,
): ViewModel() {

    private val _state = MutableStateFlow(LocalityUiState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            localityId?.let {
                with(localityRepository.getLocality(localityId)) {
                    _state.update { it.copy(
                        localityEntity = this,
                        localityName = localityName,
                        numSessions = numSessions.toString(),
                        note = note ?: "",
                        latitudeA = latitudeA?.toString() ?: "",
                        longitudeA = longitudeA?.toString() ?: "",
                        latitudeB = latitudeB?.toString() ?: "",
                        longitudeB = longitudeB?.toString() ?: "",
                    ) }
                }
            }
            _state.update { it.copy(
                isLoading = false,
            ) }
        }
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
                    note = event.text,
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
                localityNameError = "Locality must have a name",
            ) }
            return
        }
        val numSessions = Integer.parseInt(state.value.numSessions.ifEmpty { "0" })
        val latitudeA = state.value.latitudeA.ifEmpty { null }
        val longitudeA = state.value.longitudeA.ifEmpty { null }
        val latitudeB = state.value.latitudeB.ifEmpty { null }
        val longitudeB = state.value.longitudeB.ifEmpty { null }
        val note = state.value.note.ifEmpty { null }

        val currentLocality = state.value.localityEntity
        val localityEntity = if (currentLocality == null) {
            LocalityEntity(
                localityName = localityName,
                numSessions = numSessions,
                latitudeA = latitudeA?.toFloat(),
                longitudeA = longitudeA?.toFloat(),
                latitudeB = latitudeB?.toFloat(),
                longitudeB = longitudeB?.toFloat(),
                localityDateTimeCreated = ZonedDateTime.now(),
                localityDateTimeUpdated = null,
                note = note,
            )
        } else {
            LocalityEntity(
                localityId = currentLocality.localityId,
                localityName = localityName,
                numSessions = numSessions,
                latitudeA = latitudeA?.toFloat(),
                longitudeA = longitudeA?.toFloat(),
                latitudeB = latitudeB?.toFloat(),
                longitudeB = longitudeB?.toFloat(),
                localityDateTimeCreated = currentLocality.localityDateTimeCreated,
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
                        error = result.throwable?.message.toString(),
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
                        error = result.throwable?.message.toString(),
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