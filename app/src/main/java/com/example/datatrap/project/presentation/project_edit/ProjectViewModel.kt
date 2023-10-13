package com.example.datatrap.project.presentation.project_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.project.data.ProjectEntity
import com.example.datatrap.project.data.ProjectRepository
import com.example.datatrap.project.navigation.ProjectScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _state = MutableStateFlow(ProjectUiState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentProject: ProjectEntity? = null

    init {
        _state.update { it.copy(
            isLoading = true,
        ) }
        savedStateHandle.get<String>(ProjectScreens.ProjectScreen.projectIdKey)?.let { id ->
            viewModelScope.launch {
                with(projectRepository.getProjectById(id)) {
                    currentProject = this
                    _state.update { it.copy(
                        isLoading = false,
                        selectedProject = this,
                    ) }
                }
            }
        }

    }

    fun onEvent(event: ProjectScreenEvent) {
        when(event) {
            is ProjectScreenEvent.OnInsertClick -> insertProject()
            is ProjectScreenEvent.OnNumberLocalChange -> {
                _state.update { it.copy(
                    numLocal = event.text,
                    numLocalError = null,
                ) }
            }
            is ProjectScreenEvent.OnNumberMiceChange -> {
                _state.update { it.copy(
                    numMice = event.text,
                    numMiceError = null,
                ) }
            }
            is ProjectScreenEvent.OnProjectNameChange -> {
                _state.update { it.copy(
                    projectName = event.text,
                    projectNameError = null,
                ) }
            }
        }

    }

    private fun insertProject() {

        val projectEntity: ProjectEntity = ProjectEntity(
            projectName = state.value.projectName.run {
                this.ifEmpty {
                    _state.update { it.copy(
                        projectNameError = "Project must have a name."
                    ) }
                    return
                }
            },
            numLocal = state.value.numLocal.run {
                val value = this.ifEmpty { "0" }
                Integer.parseInt(value)
            },
            numMice = state.value.numMice.run {
                val value = this.ifEmpty { "0" }
                Integer.parseInt(value)
            },
            projectDateTimeCreated = currentProject?.projectDateTimeCreated ?: ZonedDateTime.now(),
            projectDateTimeUpdated = if(currentProject == null) null else ZonedDateTime.now(),
        )

        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.insertProject(projectEntity)
            _eventFlow.emit(UiEvent.NavigateBack)
        }

    }

}