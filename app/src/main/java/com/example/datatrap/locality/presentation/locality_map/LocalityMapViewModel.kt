package com.example.datatrap.locality.presentation.locality_map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.locality.data.locality.LocalityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class LocalityMapViewModel(
    localityRepository: LocalityRepository,
): ViewModel() {

    private val _state = MutableStateFlow(LocalityMapUiState())
    val state = _state.asStateFlow()

    init {
        localityRepository.getLocalities().onEach { localList ->
            _state.update { it.copy(
                localityList = localList,
            ) }
        }.launchIn(viewModelScope)
    }
}