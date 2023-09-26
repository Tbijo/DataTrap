package com.example.datatrap.specie.presentation.specie_add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.specie.data.SpecieEntity
import com.example.datatrap.specie.data.SpecieRepository
import com.example.datatrap.specie.data.specie_image.SpecieImageRepository
import com.example.datatrap.specie.navigation.SpecieScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class SpecieViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val specieRepository: SpecieRepository,
    private val specieImageRepository: SpecieImageRepository,
): ViewModel() {

    private val _state = MutableStateFlow(SpecieUiState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val specieId = savedStateHandle.get<String>(SpecieScreens.SpecieScreen.specieIdKey)

            specieId?.let {
                val specie = specieRepository.getSpecie(specieId)
                _state.update { it.copy(
                    specieEntity = specie,
                    specieCode = specie.speciesCode,
                    fullName = specie.fullName,
                    synonym = specie.synonym ?: "",
                    authority = specie.authority ?: "",
                    description = specie.description ?: "",
                    isSmall = specie.isSmallMammal,
                    numOfFingers = specie.upperFingers,
                    minWeight = specie.minWeight?.let { value -> "$value" } ?: "",
                    maxWeight = specie.maxWeight?.let { value -> "$value" } ?: "",
                    bodyLength = specie.bodyLength?.let { value -> "$value" } ?: "",
                    tailLength = specie.tailLength?.let { value -> "$value" } ?: "",
                    minFeetLength = specie.feetLengthMin?.let { value -> "$value" } ?: "",
                    maxFeetLength = specie.feetLengthMax?.let { value -> "$value" } ?: "",
                    note = specie.note ?: "",
                ) }
            }
        }
    }

    fun onEvent(event: SpecieScreenEvent) {
        when(event) {
            SpecieScreenEvent.OnCameraClick -> TODO()
            SpecieScreenEvent.OnInsertClick -> insertSpecie()
            is SpecieScreenEvent.OnAuthorityTextChanged -> {
                _state.update { it.copy(
                    authority = event.text,
                    authorityError = null,
                ) }
            }
            is SpecieScreenEvent.OnBodyLenTextChanged -> {
                _state.update { it.copy(
                    bodyLength = event.text,
                ) }
            }
            is SpecieScreenEvent.OnDescriptionTextChanged -> {
                _state.update { it.copy(
                    description = event.text,
                    descriptionError = null,
                ) }
            }
            is SpecieScreenEvent.OnFullNameTextChanged -> {
                _state.update { it.copy(
                    fullName = event.text,
                    fullNameError = null,
                ) }
            }
            is SpecieScreenEvent.OnMaxFeetLenTextChanged -> {
                _state.update { it.copy(
                    maxFeetLength = event.text,
                ) }
            }
            is SpecieScreenEvent.OnMaxWeightTextChanged -> {
                _state.update { it.copy(
                    maxWeight = event.text,
                ) }
            }
            is SpecieScreenEvent.OnMinFeetLenTextChanged -> {
                _state.update { it.copy(
                    minFeetLength = event.text,
                ) }
            }
            is SpecieScreenEvent.OnMinWeightTextChanged -> {
                _state.update { it.copy(
                    minWeight = event.text,
                ) }
            }
            is SpecieScreenEvent.OnNoteTextChanged -> {
                _state.update { it.copy(
                    note = event.text,
                ) }
            }
            is SpecieScreenEvent.OnSpecieCodeTextChanged -> {
                _state.update { it.copy(
                    specieCode = event.text,
                    specieCodeError = null,
                ) }
            }
            is SpecieScreenEvent.OnSynonymTextChanged -> {
                _state.update { it.copy(
                    synonym = event.text,
                    synonymError = null,
                ) }
            }
            is SpecieScreenEvent.OnTailLenTextChanged -> {
                _state.update { it.copy(
                    tailLength = event.text,
                ) }
            }
            is SpecieScreenEvent.OnNumFingersClick -> {
                _state.update { it.copy(
                    numOfFingers = event.numFingers,
                ) }
            }
            SpecieScreenEvent.OnIsSmallClick -> {
                _state.update { it.copy(
                    isSmall = !state.value.isSmall,
                ) }
            }
        }
    }

    private fun insertSpecie() {
        val speciesCode = state.value.specieCode.ifEmpty {
            _state.update { it.copy(
                specieCodeError = "Specie code can not be empty.",
            ) }
            return
        }

        val fullName = state.value.fullName.ifEmpty {
            _state.update { it.copy(
                fullNameError = "Full name can not be empty.",
            ) }
            return
        }

        val authority = state.value.authority.ifBlank { null }
        val synonym = state.value.synonym.ifBlank { null }
        val description = state.value.description.ifBlank { null }
        val upperFingers = state.value.numOfFingers
        val isSmallMammal: Boolean = state.value.isSmall
        val minWeight = state.value.minWeight.toFloatOrNull()
        val maxWeight = state.value.maxWeight.toFloatOrNull()

        val bodyLen = state.value.bodyLength.toFloatOrNull()
        val tailLen = state.value.tailLength.toFloatOrNull()
        val feetMinLen = state.value.minFeetLength.toFloatOrNull()
        val feetMaxLen = state.value.maxFeetLength.toFloatOrNull()

        val note = state.value.note.ifBlank { null }

        val currentSpecie = state.value.specieEntity
        val specieEntity: SpecieEntity = if (currentSpecie == null) {
            SpecieEntity(
                speciesCode = speciesCode,
                fullName = fullName,
                authority = authority,
                synonym = synonym,
                description = description,
                isSmallMammal = isSmallMammal,
                upperFingers = upperFingers,
                minWeight = minWeight,
                maxWeight = maxWeight,

                bodyLength = bodyLen,
                tailLength = tailLen,
                feetLengthMin = feetMinLen,
                feetLengthMax = feetMaxLen,

                note = note,
                specieDateTimeCreated = ZonedDateTime.now(),
                specieDateTimeUpdated = null,
            )
        } else {
            SpecieEntity(
                specieId = currentSpecie.specieId,
                speciesCode = speciesCode,
                fullName = fullName,
                authority = authority,
                synonym = synonym,
                description = description,
                isSmallMammal = isSmallMammal,
                upperFingers = upperFingers,
                minWeight = minWeight,
                maxWeight = maxWeight,

                bodyLength = bodyLen,
                tailLength = tailLen,
                feetLengthMin = feetMinLen,
                feetLengthMax = feetMaxLen,

                note = note,
                specieDateTimeCreated = currentSpecie.specieDateTimeCreated,
                specieDateTimeUpdated = ZonedDateTime.now(),
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            specieRepository.insertSpecie(specieEntity)
            _eventFlow.emit(UiEvent.NavigateBack)
        }
    }

}