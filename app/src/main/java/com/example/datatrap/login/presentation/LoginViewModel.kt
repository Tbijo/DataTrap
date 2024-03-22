package com.example.datatrap.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.core.data.shared_nav_args.NavArgsStorage
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.core.util.ScienceTeam
import com.example.datatrap.settings.user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val navArgsStorage: NavArgsStorage,
): ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        syncDateOnFirstUse()
        _state.update { it.copy(
            isLoading = false,
        ) }
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

            LoginScreenEvent.OnDismissDialog -> onDismissDialog()

            is LoginScreenEvent.OnPermissionResult -> onPermissionResult(
                isGranted = event.isGranted,
                permission = event.permission,
            )
        }
    }

    private fun logIn() {
        val userName = state.value.userName.ifEmpty {
            _state.update { it.copy(
                userNameError = "Type a name",
            ) }
            return
        }

        val pass = state.value.password.ifEmpty {
            _state.update { it.copy(
                passwordError = "Type a password",
            ) }
            return
        }

        val team: ScienceTeam = if (state.value.selectedTeam == null) {
            _state.update { it.copy(
                error = "Select a team.",
            ) }
            return
        } else {
            state.value.selectedTeam!!
        }

        viewModelScope.launch(Dispatchers.IO) {
            with(userRepository.checkUser(userName, pass)) {
                this?.let {
                    // save active user
                    navArgsStorage.saveUserId(it)
                    // save selected team
                    navArgsStorage.saveUserTeam(team.numTeam)
                    // navigate to project screen
                    _eventFlow.emit(UiEvent.NavigateNext)
                }
                // error message
                    ?: _state.update { it.copy(
                        error = "Wrong name or password.",
                    ) }
            }
        }
    }

    private fun syncDateOnFirstUse() {
        viewModelScope.launch(Dispatchers.IO) {
            // If the user starts app first time - create timestamp for sync
            if (navArgsStorage.readLastSyncDate() == null) {
                navArgsStorage.saveLastSyncDate(
                    ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                )
            }
        }
    }

    private fun onPermissionResult(isGranted: Boolean, permission: String) {
        // call this function when we get permission results

        // if the permission was not granted we want to put it into our queue on the first index
        // And the permission should not be duplicated in our queue
        if(!isGranted && !state.value.visiblePermissionDialogQueue.contains(permission)) {
            val newList = state.value.visiblePermissionDialogQueue
            newList.add(permission)

            _state.update { it.copy(
                visiblePermissionDialogQueue = newList,
            ) }
        }
    }

    private fun onDismissDialog() {
        // for dismissing a dialog by clicking OK or outside the dialog
        // We want to pop the entry of our queue
        val newList = state.value.visiblePermissionDialogQueue
        newList.removeFirst()

        _state.update { it.copy(
            visiblePermissionDialogQueue = newList,
        ) }
    }

}