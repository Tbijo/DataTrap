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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
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
                sessionRepository.getSession(sessionId).onEach { session ->
                    _state.update { it.copy(
                        sessionNum = session.session.toString()
                    ) }
                }
            }
        }.launchIn(viewModelScope)

        savedStateHandle.getStateFlow<String?>(OccasionScreens.OccasionListScreen.localityIdKey, null).onEach { locId ->
            locId?.let { localityId ->
                localityRepository.getLocality(localityId).onEach { locality ->
                    _state.update { it.copy(
                        localityName = locality.localityName
                    ) }
                }
            }
        }.launchIn(viewModelScope)

        savedStateHandle.getStateFlow<String?>(OccasionScreens.OccasionListScreen.projectIdKey, null).onEach { prjId ->
            prjId?.let { projectId ->
                projectRepository.getProjectById(projectId).onEach { project ->
                    _state.update { it.copy(
                        projectName = project.projectName,
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
        val imagePath = ""
        occasionImageRepository.getImageForOccasion(occasionEntity.occasionId).onEach { imageEntity ->
            imageEntity?.let {
                // delete image file
                val myFile = File(it.path)
                if (myFile.exists()) myFile.delete()
            }
            occasionRepository.deleteOccasion(occasionEntity, imagePath)
        }.launchIn(viewModelScope)
    }

}