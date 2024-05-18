package com.example.datatrap.occasion.presentation.occasion_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.occasion.data.occasion.OccasionEntity
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.occasion.domain.use_case.DeleteOccasionUseCase
import com.example.datatrap.project.data.ProjectRepository
import com.example.datatrap.session.data.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OccasionListViewModel(
    private val occasionRepository: OccasionRepository,
    private val projectRepository: ProjectRepository,
    private val sessionRepository: SessionRepository,
    private val localityRepository: LocalityRepository,
    private val deleteOccasionUseCase: DeleteOccasionUseCase,
    private val sessionId: String?,
    private val localityId: String?,
): ViewModel() {

    private val _state = MutableStateFlow(OccasionListUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            sessionId?.let { sessionId ->
                val session = sessionRepository.getSession(sessionId)

                occasionRepository.getOccasionsForSession(sessionId).collect { occasionList ->
                    _state.update { it.copy(
                        occasionList = occasionList,
                    ) }
                }

                _state.update { it.copy(
                    projectName = projectRepository.getProjectById(session.projectID).projectName,
                    sessionNum = session.toString(),
                ) }
            }

            localityId?.let { localityId ->
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
            deleteOccasionUseCase(
                occasionId = occasionEntity.occasionId,
                sessionID = occasionEntity.sessionID,
            )
        }
    }

}