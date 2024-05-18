package com.example.datatrap.session.presentation.session_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.session.data.SessionEntity
import com.example.datatrap.session.data.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class SessionViewModel(
    private val sessionRepository: SessionRepository,
    private val sessionId: String?,
    private val projectId: String?,
): ViewModel() {

    private val _state = MutableStateFlow(SessionUiState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            sessionId?.let {
                with(sessionRepository.getSession(sessionId)) {
                    _state.update { it.copy(
                        session = this,
                        sessionNum = session.toString(),
                        numOcc = numOcc.toString(),
                    ) }
                }
            }

            _state.update { it.copy(
                isLoading = false,
            ) }
        }
    }

    fun onEvent(event: SessionScreenEvent) {
        when(event) {
            SessionScreenEvent.OnInsertClick -> insertSession()

            is SessionScreenEvent.OnNumberOccasionChange -> {
                _state.update { it.copy(
                    numOcc = event.text,
                    numOccError = null,
                ) }
            }

            is SessionScreenEvent.OnSessionNumberChange -> {
                _state.update { it.copy(
                    sessionNum = event.text,
                    sessionNumError = null,
                ) }
            }
        }
    }

    private fun insertSession() {
        val session = state.value.sessionNum.ifEmpty {
            _state.update { it.copy(
                sessionNumError = "Session must have a number.",
            ) }
            return
        }.toInt()

        val occasionNum = state.value.numOcc.ifEmpty {
            _state.update { it.copy(
                numOccError = "Occasion must have at least 0.",
            ) }
            return
        }.toInt()

        if (projectId == null) {
            _state.update { it.copy(
                error = "This should not happen.",
            ) }
            return
        }

        val currentSessionEntity = state.value.session
        val sessionEntity = if (currentSessionEntity == null) {
            SessionEntity(
                session = session,
                numOcc = occasionNum,
                projectID = projectId,
                sessionStart = ZonedDateTime.now(),
                sessionDateTimeCreated = ZonedDateTime.now(),
                sessionDateTimeUpdated = null,
            )
        } else {
            SessionEntity(
                sessionId = currentSessionEntity.sessionId,
                session = session,
                numOcc = occasionNum,
                projectID = projectId,
                sessionStart = currentSessionEntity.sessionStart,
                sessionDateTimeCreated = currentSessionEntity.sessionDateTimeCreated,
                sessionDateTimeUpdated = ZonedDateTime.now(),
            )
        }

        viewModelScope.launch {
            sessionRepository.insertSession(sessionEntity)
            // navigate back
            _eventFlow.emit(UiEvent.NavigateBack)
        }
    }
}