package com.example.datatrap.locality.presentation.locality_list

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
            is LocalityListScreenEvent.OnSearchTextChange -> searchProjects(event.text)
            is LocalityListScreenEvent.ChangeTitleFocus -> {
                _state.update { it.copy(
                    isSearchTextFieldHintVisible = !event.focusState.isFocused
                            && state.value.searchTextFieldValue.isBlank(),
                ) }
            }
            is LocalityListScreenEvent.OnDeleteClick -> deleteLocality(event.localityEntity)
            LocalityListScreenEvent.OnDismissDialog -> onDismissDialog()

            is LocalityListScreenEvent.OnPermissionResult -> onPermissionResult(
                isGranted = event.isGranted,
                permission = event.permission,
            )

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

    private fun onPermissionResult(isGranted: Boolean, permission: String) {
        // call this function when we get permission results

        // if the permission was not granted we want to put it into our queue on the first index
        // And the permission should not be duplicated in our queue
        if(!isGranted && !state.value.visiblePermissionDialogQueue.contains(permission)) {
            val newList = state.value.visiblePermissionDialogQueue
            newList.add(permission)

            _state.update { it.copy(
                visiblePermissionDialogQueue = newList,
            ) }
        }
    }

    private fun onDismissDialog() {
        // for dismissing a dialog by clicking OK or outside the dialog
        // We want to pop the entry of our queue
        val newList = state.value.visiblePermissionDialogQueue
        newList.removeFirst()

        _state.update { it.copy(
            visiblePermissionDialogQueue = newList
        ) }
    }
}