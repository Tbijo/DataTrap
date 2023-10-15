package com.example.datatrap.session.presentation.session_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.core.domain.use_case.InsertLocalitySessionUseCase
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.project.data.ProjectRepository
import com.example.datatrap.session.data.SessionEntity
import com.example.datatrap.session.data.SessionRepository
import com.example.datatrap.session.navigation.SessionScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class SessionListViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val localityRepository: LocalityRepository,
    private val projectRepository: ProjectRepository,
    private val localitySessionUseCase: InsertLocalitySessionUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(SessionListUiState())
    val state = _state.asStateFlow()

    val localityID = savedStateHandle.get<String>(SessionScreens.SessionListScreen.localityIdKey)
    val projectID = savedStateHandle.get<String>(SessionScreens.SessionListScreen.projectIdKey)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(
                isLoading = true,
            ) }

            if (!projectID.isNullOrEmpty() && !localityID.isNullOrEmpty()) {
                val project = projectRepository.getProjectById(projectID)
                val locality = localityRepository.getLocality(localityID)

                _state.update { it.copy(
                    sessionList = sessionRepository.getSessionsForProject(projectID),
                    isLoading = false,
                    projectName = project.projectName,
                    projectId = project.projectId,
                    localityName = locality.localityName,
                    localityId = locality.localityId
                ) }
            }
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
                error = "This should not happen."
            ) }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            localitySessionUseCase(
                localityId = localityID,
                sessionId = sessionId,
            )
        }
    }

    private fun insertSession() {
        viewModelScope.launch(Dispatchers.IO) {
            sessionRepository.insertSession(
                SessionEntity(
                    session = (state.value.sessionList.size + 1),
                    projectID = state.value.projectId,
                    numOcc = 0,
                    sessionDateTimeCreated = ZonedDateTime.now(),
                    sessionDateTimeUpdated = null
                )
            )
        }
    }

    private fun deleteSession(sessionEntity: SessionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            sessionRepository.deleteSession(sessionEntity)
        }
    }

}