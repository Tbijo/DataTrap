package com.example.datatrap.session.presentation.session_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.session.data.SessionEntity
import com.example.datatrap.session.data.SessionRepository
import com.example.datatrap.session.navigation.SessionScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _state = MutableStateFlow(SessionUiState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        _state.update { it.copy(
            isLoading = true
        ) }

        savedStateHandle.getStateFlow<String?>(
            key = SessionScreens.SessionScreen.sessionIdKey,
            initialValue = null,
        ).onEach { sessionId ->
            sessionId?.let { sesId ->
                with(sessionRepository.getSession(sesId)) {
                    _state.update { it.copy(
                        isLoading = false,
                        session = this,
                        sessionNum = session.toString(),
                        numOcc = numOcc.toString(),
                    ) }
                }
            }
        }.launchIn(viewModelScope)

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
        if (state.value.sessionNum.isNotEmpty() && state.value.numOcc.isNotEmpty()) return

        val sessionEntity = SessionEntity(
            sessionId = state.value.session?.sessionId ?: "",
            session = Integer.parseInt(state.value.sessionNum.ifEmpty {
                _state.update { it.copy(
                    sessionNumError = "Session must have a number.",
                ) }
                return
            }),
            numOcc = Integer.parseInt(state.value.numOcc.ifEmpty {
                _state.update { it.copy(
                    numOccError = "Occasion must have at least 0.",
                ) }
                return
            }),
            projectID = state.value.session?.projectID ?: "",
            sessionDateTimeCreated = state.value.session?.sessionDateTimeCreated ?: ZonedDateTime.now(),
            sessionDateTimeUpdated = ZonedDateTime.now(),
        )

        viewModelScope.launch {
            sessionRepository.insertSession(sessionEntity)
            // navigate back
            _eventFlow.emit(UiEvent.NavigateBack)
        }
    }
}