package com.example.datatrap.mouse.presentation.mouse_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.camera.data.mouse_image.MouseImageRepository
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.mouse.domain.use_case.GetMouseDetail
import com.example.datatrap.mouse.domain.use_case.GetPreviousLogsOfMouse
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
class MouseDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mouseRepository: MouseRepository,
    private val specieRepository: SpecieRepository,
    private val occasionRepository: OccasionRepository,
    private val sessionRepository: SessionRepository,
    private val projectRepository: ProjectRepository,
    private val localityRepository: LocalityRepository,
    private val mouseImageRepository: MouseImageRepository,
    private val getMouseDetail: GetMouseDetail = GetMouseDetail(
        mouseRepository = mouseRepository,
        specieRepository = specieRepository,
        occasionRepository = occasionRepository,
        sessionRepository = sessionRepository,
        projectRepository = projectRepository,
    ),
    private val getPreviousLogsOfMouse: GetPreviousLogsOfMouse = GetPreviousLogsOfMouse(
        mouseRepository = mouseRepository,
        localityRepository = localityRepository,
    ),
): ViewModel() {

    private val _state = MutableStateFlow(MouseDetailUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val mouseId = savedStateHandle.get<String>(MouseScreens.MouseDetailScreen.mouseIdKey)

            mouseId?.let {
                getMouseDetail(mouseId).collect { mouseView ->
                    _state.update { it.copy(
                        mouseView = mouseView,
                    ) }

                    val id = mouseView.primeMouseId ?: mouseView.mouseId
                    getPreviousLogsOfMouse(id).collect { mouseLogList ->
                        _state.update { it.copy(
                            logList = mouseLogList,
                        ) }
                    }
                }

                _state.update { it.copy(
                    mouseImagePath = mouseImageRepository.getImageForMouse(mouseId)?.path,
                ) }
            }
        }
    }

    fun onEvent(event: MouseDetailScreenEvent) {
        when(event) {
            MouseDetailScreenEvent.OnImageClick -> {
                // TODO Display the big picture, Dialog maybe?
            }
        }
    }
}