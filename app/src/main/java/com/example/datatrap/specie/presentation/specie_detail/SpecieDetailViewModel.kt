package com.example.datatrap.specie.presentation.specie_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.specie.data.SpecieRepository
import com.example.datatrap.specie.data.specie_image.SpecieImageRepository
import com.example.datatrap.specie.navigation.SpecieScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpecieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val specieRepository: SpecieRepository,
    private val specieImageRepository: SpecieImageRepository,
): ViewModel() {

    private val _state = MutableStateFlow(SpecieDetailUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val specieId = savedStateHandle.get<String>(SpecieScreens.SpecieDetailScreen.specieIdKey)
            specieId?.let {
                val specie = specieRepository.getSpecie(specieId)

                _state.update { it.copy(
                    specieEntity = specie,
                ) }

                val specieImage = specieImageRepository.getImageForSpecie(specieId)

                specieImage?.let {
                    _state.update { it.copy(
                        imagePath = specieImage.path,
                    ) }
                }
            }
        }
    }

    fun onEvent(event: SpecieDetailScreenEvent) {
        when(event) {
            SpecieDetailScreenEvent.OnImageClick -> {
                // TODO Display the big picture, Dialog maybe?
            }
        }
    }
}