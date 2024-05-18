package com.example.datatrap.locality.presentation.locality_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.core.domain.use_case.InsertProjectLocalityUseCase
import com.example.datatrap.locality.data.locality.LocalityEntity
import com.example.datatrap.locality.data.locality.LocalityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocalityListViewModel(
    private val localityRepository: LocalityRepository,
    private val insertProjectLocalityUseCase: InsertProjectLocalityUseCase,
    val projectID: String,
): ViewModel() {

    private val _state = MutableStateFlow(LocalityListUiState())
    val state = _state.asStateFlow()



    init {
        _state.update { it.copy(
            projectId = projectID,
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
            is LocalityListScreenEvent.SetNumLocalOfProject -> setNumLocalOfProject(event.localityId)

            else -> Unit
        }
    }

    private fun setNumLocalOfProject(localityId: String) {
        if (projectID == null) {
            _state.update { it.copy(
                error = "This should not happen.",
            ) }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            insertProjectLocalityUseCase(
                localityId = localityId,
                projectId = projectID,
            )
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