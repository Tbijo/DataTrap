package com.example.datatrap.session.presentation.session_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.project.data.ProjectRepository
import com.example.datatrap.session.data.SessionEntity
import com.example.datatrap.session.data.SessionRepository
import com.example.datatrap.session.navigation.SessionScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class SessionListViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val localityRepository: LocalityRepository,
    private val projectRepository: ProjectRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(SessionListUiState())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(
            isLoading = true,
        ) }

        val projectId = savedStateHandle.getStateFlow<String?>(
            key = SessionScreens.SessionListScreen.projectIdKey,
            initialValue = null,
        )
        val localityId = savedStateHandle.getStateFlow<String?>(
            key = SessionScreens.SessionListScreen.localityIdKey,
            initialValue = null,
        )

        projectId.zip(localityId) { projId, locId ->
            if (!projId.isNullOrEmpty() && !locId.isNullOrEmpty()) {
                val project = projectRepository.getProjectById(projId)
                val locality = localityRepository.getLocality(locId)

                _state.update { it.copy(
                    sessionList = sessionRepository.getSessionsForProject(projId),
                    isLoading = false,
                    projectName = project.projectName,
                    projectId = project.projectId,
                    localityName = locality.localityName,
                    localityId = locality.localityId
                ) }
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: SessionListScreenEvent) {
        when(event) {
            is SessionListScreenEvent.OnAddButtonClick -> insertSession()
            is SessionListScreenEvent.OnDeleteClick -> deleteSession(event.sessionEntity)
            else -> Unit
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