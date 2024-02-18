package com.example.datatrap.specie.presentation.specie_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.specie.data.SpecieRepository
import com.example.datatrap.specie.data.specie_image.SpecieImageRepository
import com.example.datatrap.specie.domain.use_case.DeleteSpecieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpecieListViewModel @Inject constructor(
    private val specieRepository: SpecieRepository,
    private val specieImageRepository: SpecieImageRepository,
    private val deleteSpecieUseCase: DeleteSpecieUseCase,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _state = MutableStateFlow(SpecieListUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(
                specieList = specieRepository.getSpecies(),
                isLoading = false,
            ) }
        }
    }

    fun onEvent(event: SpecieListScreenEvent) {
        when(event) {
            is SpecieListScreenEvent.OnDeleteClick -> deleteSpecie(event.specieEntity.specieId)

            is SpecieListScreenEvent.OnSearchTextChange -> searchSpecies(event.text)

            is SpecieListScreenEvent.ChangeTitleFocus -> {
                _state.update { it.copy(
                    isSearchTextFieldHintVisible = !event.focusState.isFocused
                            && state.value.searchTextFieldValue.isBlank(),
                ) }
            }

            else -> Unit
        }
    }

    private fun deleteSpecie(specieId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteSpecieUseCase(specieId)
        }
    }

    private fun searchSpecies(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val searchQuery = "%$query%"
            _state.update { it.copy(
                specieList = specieRepository.searchSpecies(searchQuery),
            ) }
        }
    }
}