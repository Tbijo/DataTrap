package com.example.datatrap.locality.presentation.locality_list

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.locality.data.LocalityEntity
import com.example.datatrap.locality.data.LocalityRepository
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
    private val localityRepository: LocalityRepository
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
            is LocalityListScreenEvent.OnDeleteClick -> deleteLocality(event.localityEntity)
            is LocalityListScreenEvent.OnSearchTextChange -> searchProjects(event.text)
            is LocalityListScreenEvent.ChangeTitleFocus -> {
                _state.update { it.copy(
                    isSearchTextFieldHintVisible = !event.focusState.isFocused
                            && state.value.searchTextFieldValue.isBlank(),
                ) }
            }
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

    ///////////////////////////////PERMISSIONS////////////////////////////////////////

    // We want to show multiple dialogs (one after the other) because the user may decline all of the permissions
    // We need to queue these dialogs, queue data structure
    // String will be the permission
    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    // for dismissing a dialog by clicking OK or outside the dialog
    // We want to pop the entry of our queue
    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    // call this function when we get permission results
    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        // if the permission was not granted we want to put it into our queue on the first index
        // And the permission should not be duplicated in our queue
        if(!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }
}