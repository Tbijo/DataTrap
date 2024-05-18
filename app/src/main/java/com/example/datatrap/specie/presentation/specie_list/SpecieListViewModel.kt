package com.example.datatrap.specie.presentation.specie_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.specie.data.SpecieRepository
import com.example.datatrap.specie.domain.use_case.DeleteSpecieUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpecieListViewModel(
    private val specieRepository: SpecieRepository,
    private val deleteSpecieUseCase: DeleteSpecieUseCase,
): ViewModel() {

    private val _state = MutableStateFlow(SpecieListUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            specieRepository.getSpecies().collect { species ->
                _state.update { it.copy(
                    specieList = species,
                    isLoading = false,
                ) }
            }
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
            specieRepository.searchSpecies(query).collect { species ->
                _state.update { it.copy(
                    specieList = species,
                ) }
            }
        }
    }
}