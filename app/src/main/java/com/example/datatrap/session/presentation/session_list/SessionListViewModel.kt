package com.example.datatrap.session.presentation.session_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.core.domain.use_case.InsertLocalitySessionUseCase
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.project.data.ProjectRepository
import com.example.datatrap.session.data.SessionEntity
import com.example.datatrap.session.data.SessionRepository
import com.example.datatrap.session.domain.DeleteSessionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class SessionListViewModel(
    private val sessionRepository: SessionRepository,
    private val localityRepository: LocalityRepository,
    private val projectRepository: ProjectRepository,
    private val insertLocalitySessionUseCase: InsertLocalitySessionUseCase,
    private val deleteSessionUseCase: DeleteSessionUseCase,
    private val localityID: String?,
    private val projectID: String?,
) : ViewModel() {

    private val _state = MutableStateFlow(SessionListUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (!projectID.isNullOrEmpty() && !localityID.isNullOrEmpty()) {
                val locality = localityRepository.getLocality(localityID)
                val project = projectRepository.getProjectById(projectID)

                sessionRepository.getSessionsForProject(projectID).collect { sessionList ->
                    _state.update { it.copy(
                        sessionList = sessionList,
                    ) }
                }

                _state.update { it.copy(
                    projectName = project.projectName,
                    localityName = locality.localityName,
                    localityId = localityID,
                ) }
            }

            _state.update { it.copy(
                isLoading = false,
            ) }
        }
    }

    fun onEvent(event: SessionListScreenEvent) {
        when(event) {
            is SessionListScreenEvent.OnAddButtonClick -> insertSession()
            is SessionListScreenEvent.OnDeleteClick -> deleteSession(event.sessionEntity)
            is SessionListScreenEvent.SetSesNumInLocality -> setSesNumInLocality(event.sessionId)
            else -> Unit
        }
    }

    private fun setSesNumInLocality(sessionId: String) {
        if (localityID == null) {
            _state.update { it.copy(
                error = "This should not happen.",
            ) }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            insertLocalitySessionUseCase(
                localityId = localityID,
                sessionId = sessionId,
            )
        }
    }

    private fun insertSession() {
        if (projectID == null) {
            _state.update { it.copy(
                error = "This should not happen.",
            ) }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            sessionRepository.insertSession(
                SessionEntity(
                    session = (state.value.sessionList.size + 1),
                    projectID = projectID,
                    numOcc = 0,
                    sessionDateTimeCreated = ZonedDateTime.now(),
                    sessionDateTimeUpdated = null,
                )
            )
        }
    }

    private fun deleteSession(sessionEntity: SessionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteSessionUseCase(
                sessionId = sessionEntity.sessionId,
                projectID = sessionEntity.projectID,
            )
            sessionRepository.deleteSession(sessionEntity)
        }
    }

}