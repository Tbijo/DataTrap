package com.example.datatrap.locality.presentation.locality_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.locality.data.locality.LocalityEntity
import com.example.datatrap.locality.data.locality.LocalityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalityListViewModel @Inject constructor (
    private val localityRepository: LocalityRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _state = MutableStateFlow(LocalityListUiState())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(
            isLoading = true
        ) }

        localityRepository.getLocalities().onEach { locList ->
            _state.update { it.copy(
                isLoading = false,
                localityList = locList,
            ) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: LocalityListScreenEvent) {
        when(event) {
            is LocalityListScreenEvent.OnSearchTextChange -> searchProjects(event.text)
            is LocalityListScreenEvent.ChangeTitleFocus -> {
                _state.update { it.copy(
                    isSearchTextFieldHintVisible = !event.focusState.isFocused
                            && state.value.searchTextFieldValue.isBlank(),
                ) }
            }
            is LocalityListScreenEvent.OnDeleteClick -> deleteLocality(event.localityEntity)

            else -> Unit
        }
    }

    private fun searchProjects(query: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _state.update { it.copy(
                isLoading = true,
                searchTextFieldValue = query,
            ) }
        }

        viewModelScope.launch(Dispatchers.IO) {
            val searchQuery = "%$query%"
            localityRepository.searchLocalities(searchQuery).onEach { localityList ->
                _state.update { it.copy(
                    isLoading = false,
                    localityList = localityList,
                ) }
            }
        }
    }

    private fun deleteLocality(localityEntity: LocalityEntity) {
        viewModelScope.launch(Dispatchers.IO){
            localityRepository.deleteLocality(localityEntity)
        }
    }
}