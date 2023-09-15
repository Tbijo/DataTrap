package com.example.datatrap.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.core.data.pref.PrefRepository
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.core.util.ScienceTeam
import com.example.datatrap.settings.user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val prefRepository: PrefRepository,
): ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        syncDateOnFirstUse()
    }

    fun onEvent(event: LoginScreenEvent) {
        when(event) {
            LoginScreenEvent.LogIn -> logIn()
            is LoginScreenEvent.OnPasswordChanged -> {
                _state.update { it.copy(
                    password = event.text.trim(),
                    passwordError = null,
                ) }
            }
            is LoginScreenEvent.OnSelectTeam -> {
                _state.update { it.copy(
                    selectedTeam = event.team
                ) }
            }
            is LoginScreenEvent.OnUserNameChanged -> {
                _state.update { it.copy(
                    userName = event.text.trim(),
                    userNameError = null,
                ) }
            }
        }
    }

    private fun logIn() {

        val userName = state.value.userName.ifEmpty {
            _state.update { it.copy(
                userNameError = "Type a name"
            ) }
            return
        }
        val pass = state.value.password.ifEmpty {
            _state.update { it.copy(
                passwordError = "Type a password"
            ) }
            return
        }

        val team: ScienceTeam = if (state.value.selectedTeam == null) {
            _state.update { it.copy(
                error = "Select a team."
            ) }
            return
        } else {
            state.value.selectedTeam!!
        }

        viewModelScope.launch(Dispatchers.IO) {
            userRepository.checkUser(userName, pass).collectLatest { userId ->
                userId?.let {
                    // save active user
                    prefRepository.saveUserId(it)
                    // save selected team
                    prefRepository.saveUserTeam(team.numTeam)
                    // navigate to project screen
                    _eventFlow.emit(UiEvent.NavigateBack)
                }
                // error message
                    ?: _state.update { it.copy(
                        error = "Wrong name or password."
                    ) }
            }
        }
    }

    private fun syncDateOnFirstUse() {
        viewModelScope.launch(Dispatchers.IO) {
            // If the user starts app first time - create timestamp for sync
            prefRepository.readLastSyncDate().collectLatest {
                it?.let {
                    prefRepository.saveLastSyncDate(
                        ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    )
                }
            }
        }
    }
}