package com.example.datatrap.specie.presentation.specie_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.specie.data.SpecieRepository
import com.example.datatrap.specie.data.specie_image.SpecieImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpecieDetailViewModel(
    private val specieRepository: SpecieRepository,
    private val specieImageRepository: SpecieImageRepository,
    private val specieId: String?,
): ViewModel() {

    private val _state = MutableStateFlow(SpecieDetailUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            specieId?.let {
                val specie = specieRepository.getSpecie(specieId)

                _state.update { it.copy(
                    specieEntity = specie,
                ) }

                val specieImage = specieImageRepository.getImageForSpecie(specieId)

                specieImage?.let {
                    _state.update { it.copy(
                        imagePath = specieImage.imageUri.path,
                    ) }
                }
            }

            _state.update { it.copy(
                isLoading = false,
            ) }
        }
    }

    fun onEvent(event: SpecieDetailScreenEvent) {
        when(event) {
            SpecieDetailScreenEvent.OnImageClick -> {
                _state.update { it.copy(
                    isSheetExpanded = !state.value.isSheetExpanded,
                ) }
            }
        }
    }
}