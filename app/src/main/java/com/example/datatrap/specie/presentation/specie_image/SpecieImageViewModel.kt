package com.example.datatrap.specie.presentation.specie_image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.specie.data.specie_image.SpecieImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpecieImageViewModel(
    private val specieImageRepository: SpecieImageRepository,
    private val specieId: String?,
) : ViewModel() {

    private val _state = MutableStateFlow(SpecieImageUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            specieId?.let {
                val specieImage = specieImageRepository.getImageForSpecie(specieId)

                specieImage?.let {
                    _state.update { it.copy(
                        specieImageEntity = specieImage,
                        imageStateText = "Image added",
                        note = specieImage.note,
                    ) }

                } ?:
                _state.update { it.copy(
                    imageStateText = "No Image",
                ) }
            }

            _state.update { it.copy(
                isLoading = false,
            ) }
        }
    }

    fun onEvent(event: SpecieImageScreenEvent) {
        when(event) {
            is SpecieImageScreenEvent.OnNoteTextChanged -> {
                _state.update { it.copy(
                    note = event.text,
                ) }
            }

            is SpecieImageScreenEvent.OnImageResult -> {
                _state.update { it.copy(
                    imageUri = event.uri,
                ) }
            }

            else -> Unit
        }
    }

}