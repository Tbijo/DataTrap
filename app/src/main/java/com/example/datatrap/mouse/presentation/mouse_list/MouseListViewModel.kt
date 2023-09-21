package com.example.datatrap.mouse.presentation.mouse_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.camera.data.mouse_image.MouseImageRepository
import com.example.datatrap.core.domain.GetInfoNamesUseCase
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.mouse.domain.use_case.DeleteMouseUseCase
import com.example.datatrap.mouse.domain.use_case.GetMiceByOccasion
import com.example.datatrap.mouse.navigation.MouseScreens
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.project.data.ProjectRepository
import com.example.datatrap.session.data.SessionRepository
import com.example.datatrap.specie.data.SpecieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MouseListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mouseRepository: MouseRepository,
    private val mouseImageRepository: MouseImageRepository,
    private val specieRepository: SpecieRepository,
    private val getMiceByOccasion: GetMiceByOccasion = GetMiceByOccasion(mouseRepository, specieRepository),
    private val deleteMouseUseCase: DeleteMouseUseCase = DeleteMouseUseCase(mouseRepository, mouseImageRepository),
    private val projectRepository: ProjectRepository,
    private val localityRepository: LocalityRepository,
    private val sessionRepository: SessionRepository,
    private val occasionRepository: OccasionRepository,
    private val getInfoNamesUseCase: GetInfoNamesUseCase = GetInfoNamesUseCase(
        projectRepository,
        localityRepository,
        sessionRepository,
        occasionRepository,
    ),
): ViewModel() {

    private val _state = MutableStateFlow(MouseListUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val projectId = savedStateHandle.get<String>(MouseScreens.MouseListScreen.projectIdKey)
            val localityId = savedStateHandle.get<String>(MouseScreens.MouseListScreen.localityIdKey)
            val sessionId = savedStateHandle.get<String>(MouseScreens.MouseListScreen.sessionIdKey)
            val occasionId = savedStateHandle.get<String>(MouseScreens.MouseListScreen.occasionIdKey)

            occasionId?.let {
                getMiceByOccasion(occasionId).collect { mouseList ->
                    _state.update { it.copy(
                        mouseList = mouseList,
                    ) }
                }
            }

            projectId?.let {
                getInfoNamesUseCase(
                    projectId = projectId,
                    localityId = localityId,
                    sessionId = sessionId,
                    occasionId = occasionId,
                ).collect {
                    _state.update { it.copy(
                        projectName = it.projectName,
                        localityName = it.localityName,
                        sessionNum = it.sessionNum,
                        occasionNum = it.occasionNum,
                    ) }
                }
            }

        }
    }

    fun onEvent(event: MouseListScreenEvent) {
        when(event) {
            is MouseListScreenEvent.OnDeleteClick -> deleteMouse(event.mouseId)
            else -> Unit
        }
    }

    private fun deleteMouse(mouseId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteMouseUseCase(mouseId)
        }
    }

}