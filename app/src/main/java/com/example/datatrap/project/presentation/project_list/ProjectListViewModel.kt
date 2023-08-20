package com.example.datatrap.project.presentation.project_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.project.data.ProjectEntity
import com.example.datatrap.project.data.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectListViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
): ViewModel() {

    private val _state = MutableStateFlow(ProjectListUiState())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(
            isLoading = true
        ) }
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.projectEntityList().onEach { projectList ->
                _state.update { it.copy(
                    isLoading = false,
                    projectList = projectList,
                ) }
            }
        }
    }

    fun onEvent(event: ProjectListScreenEvent) {
        when(event) {
            is ProjectListScreenEvent.OnDeleteClick -> deleteProject(event.projectEntity)
            is ProjectListScreenEvent.OnSearchTextChange -> searchProjects(event.text)
            is ProjectListScreenEvent.ChangeTitleFocus -> {
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
                searchTextFieldValue = query,
            ) }
        }

        viewModelScope.launch(Dispatchers.IO) {
            val searchQuery = "%$query%"
            projectRepository.searchProjects(searchQuery).collect { projectList ->
                _state.update { it.copy(
                    projectList = projectList,
                ) }
            }
        }
    }

    private fun deleteProject(projectEntity: ProjectEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.deleteProject(projectEntity)
        }
    }
}