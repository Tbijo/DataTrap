package com.example.datatrap.project.presentation.project_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.project.data.ProjectEntity
import com.example.datatrap.project.data.ProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProjectListViewModel(
    private val projectRepository: ProjectRepository,
): ViewModel() {

    private val _state = MutableStateFlow(ProjectListUiState())
    val state = _state.asStateFlow()

    init {
        projectRepository.projectEntityList().onEach { projectList ->
            _state.update { it.copy(
                isLoading = false,
                projectList = projectList,
            ) }
        }.launchIn(viewModelScope)
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
        _state.update { it.copy(
            searchTextFieldValue = query,
        ) }

        viewModelScope.launch(Dispatchers.IO) {
            val searchQuery = "%$query%"
            projectRepository.searchProjects(searchQuery).onEach { projectList ->
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