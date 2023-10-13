package com.example.datatrap.occasion.presentation.occasion_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.camera.data.occasion_image.OccasionImageRepository
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.occasion.data.occasion.OccasionEntity
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.occasion.navigation.OccasionScreens
import com.example.datatrap.project.data.ProjectRepository
import com.example.datatrap.session.data.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class OccasionListViewModel @Inject constructor(
    private val occasionRepository: OccasionRepository,
    private val projectRepository: ProjectRepository,
    private val occasionImageRepository: OccasionImageRepository,
    private val sessionRepository: SessionRepository,
    private val localityRepository: LocalityRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _state = MutableStateFlow(OccasionListUiState())
    val state = _state.asStateFlow()

    init {
        savedStateHandle.getStateFlow<String?>(OccasionScreens.OccasionListScreen.sessionIdKey, null).onEach { sesId ->
            sesId?.let { sessionId ->
                occasionRepository.getOccasionsForSession(sessionId).onEach { occasionList ->
                    _state.update { it.copy(
                        isLoading = false,
                        occasionList = occasionList,
                    ) }
                }
                with(sessionRepository.getSession(sessionId)) {
                    _state.update { it.copy(
                        sessionNum = session.toString(),
                    ) }
                }
            }
        }.launchIn(viewModelScope)

        savedStateHandle.getStateFlow<String?>(OccasionScreens.OccasionListScreen.localityIdKey, null).onEach { locId ->
            locId?.let { localityId ->
                with(localityRepository.getLocality(localityId)) {
                    _state.update { it.copy(
                        localityName = localityName,
                    ) }
                }
            }
        }.launchIn(viewModelScope)

        savedStateHandle.getStateFlow<String?>(OccasionScreens.OccasionListScreen.projectIdKey, null).onEach { prjId ->
            prjId?.let { projectId ->
                with(projectRepository.getProjectById(projectId)) {
                    _state.update { it.copy(
                        projectName = projectName,
                    ) }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: OccasionListScreenEvent) {
        when(event) {
            is OccasionListScreenEvent.OnDeleteClick -> deleteOccasion(event.occasionEntity)
            else -> Unit
        }
    }

    private fun deleteOccasion(occasionEntity: OccasionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            with(occasionImageRepository.getImageForOccasion(occasionEntity.occasionId)) {
                this?.let {
                    // delete image file
                    val myFile = File(it.path)
                    if (myFile.exists()) myFile.delete()
                }
                occasionRepository.deleteOccasion(occasionEntity)
            }
        }
    }

}