package com.example.datatrap.specie.presentation.specie_add_edit

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.specie.data.SpecieEntity
import com.example.datatrap.specie.data.SpecieRepository
import com.example.datatrap.specie.data.specie_image.SpecieImageRepository
import com.example.datatrap.specie.domain.use_case.InsertSpecieUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class SpecieViewModel(
    private val specieRepository: SpecieRepository,
    private val insertSpecieUseCase: InsertSpecieUseCase,
    private val specieImageRepository: SpecieImageRepository,
    private val specieId: String?,
): ViewModel() {

    private val _state = MutableStateFlow(SpecieUiState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            specieId?.let {
                val image = specieImageRepository.getImageForSpecie(specieId)

                with(specieRepository.getSpecie(specieId)) {
                    _state.update { it.copy(
                        specieEntity = this,
                        specieCode = speciesCode,
                        fullName = fullName,
                        synonym = synonym ?: "",
                        authority = authority ?: "",
                        description = description ?: "",
                        isSmall = isSmallMammal,
                        numOfFingers = upperFingers,
                        minWeight = minWeight?.let { value -> "$value" } ?: "",
                        maxWeight = maxWeight?.let { value -> "$value" } ?: "",
                        bodyLength = bodyLength?.let { value -> "$value" } ?: "",
                        tailLength = tailLength?.let { value -> "$value" } ?: "",
                        minFeetLength = feetLengthMin?.let { value -> "$value" } ?: "",
                        maxFeetLength = feetLengthMax?.let { value -> "$value" } ?: "",
                        note = note ?: "",
                        imageId = image?.specieImgId,
                        imageUri = image?.imageUri,
                        imageNote = image?.note,
                    ) }
                }
            }

            _state.update { it.copy(
                isLoading = false,
            ) }
        }
    }

    fun onEvent(event: SpecieScreenEvent) {
        when(event) {
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
            is SpecieScreenEvent.OnReceiveImageName -> {
                _state.update { it.copy(
                    imageUri = Uri.parse(event.imageName),
                    imageNote = event.imageNote,
                ) }
            }

            else -> Unit
        }
    }

    private fun insertSpecie() {
        with(state.value) {

            val speciesCode = specieCode.ifEmpty {
                _state.update { it.copy(
                    specieCodeError = "Specie code can not be empty.",
                ) }
                return
            }

            val fullName = fullName.ifEmpty {
                _state.update { it.copy(
                    fullNameError = "Full name can not be empty.",
                ) }
                return
            }

            val authority = authority.ifBlank { null }
            val synonym = synonym.ifBlank { null }
            val description = description.ifBlank { null }
            val upperFingers = numOfFingers
            val isSmallMammal: Boolean = isSmall
            val minWeight = minWeight.toFloatOrNull()
            val maxWeight = maxWeight.toFloatOrNull()

            val bodyLen = bodyLength.toFloatOrNull()
            val tailLen = tailLength.toFloatOrNull()
            val feetMinLen = minFeetLength.toFloatOrNull()
            val feetMaxLen = maxFeetLength.toFloatOrNull()

            val note = note.ifBlank { null }

            val specieEntity: SpecieEntity = if (specieEntity == null) {
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
                    specieId = specieEntity.specieId,
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
                    specieDateTimeCreated = specieEntity.specieDateTimeCreated,
                    specieDateTimeUpdated = ZonedDateTime.now(),
                )
            }

            viewModelScope.launch(Dispatchers.IO) {
                insertSpecieUseCase(
                    specieEntity = specieEntity,
                    imageUri = state.value.imageUri.toString(),
                    imageNote = state.value.imageNote,
                )
                _eventFlow.emit(UiEvent.NavigateBack)
            }
        }
    }

}