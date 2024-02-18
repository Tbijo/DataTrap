package com.example.datatrap.occasion.presentation.occasion_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.camera.domain.DeleteImageUseCase
import com.example.datatrap.core.getMainScreenNavArgs
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.occasion.data.occasion.OccasionEntity
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.occasion.domain.use_case.DeleteOccasionUseCase
import com.example.datatrap.project.data.ProjectRepository
import com.example.datatrap.session.data.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OccasionListViewModel @Inject constructor(
    private val occasionRepository: OccasionRepository,
    private val projectRepository: ProjectRepository,
    private val sessionRepository: SessionRepository,
    private val localityRepository: LocalityRepository,
    private val deleteOccasionUseCase: DeleteOccasionUseCase,
    private val deleteImageUseCase: DeleteImageUseCase,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _state = MutableStateFlow(OccasionListUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            savedStateHandle.getMainScreenNavArgs()?.sessionId?.let { sessionId ->
                val session = sessionRepository.getSession(sessionId)

                _state.update { it.copy(
                    occasionList = occasionRepository.getOccasionsForSession(sessionId),
                    projectName = projectRepository.getProjectById(session.projectID).projectName,
                    sessionNum = session.toString(),
                ) }
            }

            savedStateHandle.getMainScreenNavArgs()?.localityId?.let { localityId ->
                with(localityRepository.getLocality(localityId)) {
                    _state.update { it.copy(
                        localityName = localityName,
                    ) }
                }
            }

            _state.update { it.copy(
                isLoading = false,
            ) }
        }
    }

    fun onEvent(event: OccasionListScreenEvent) {
        when(event) {
            is OccasionListScreenEvent.OnDeleteClick -> deleteOccasion(event.occasionEntity)
            else -> Unit
        }
    }

    private fun deleteOccasion(occasionEntity: OccasionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteImageUseCase(
                occasionEntity = occasionEntity,
            )
            deleteOccasionUseCase(
                occasionId = occasionEntity.occasionId,
                sessionID = occasionEntity.sessionID,
            )
        }
    }

}