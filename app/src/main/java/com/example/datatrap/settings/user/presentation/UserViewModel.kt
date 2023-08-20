package com.example.datatrap.settings.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.settings.user.data.UserEntity
import com.example.datatrap.settings.user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UserScreenUiState())
    val state = _state.asStateFlow()

    init {

        _state.update { it.copy(
            isLoading = true,
        ) }

        userRepository.getUserEntityList().onEach { userList ->
            _state.update { it.copy(
                isLoading = false,
                userEntityList = userList,
            ) }
        }.launchIn(viewModelScope)

    }

    fun onEvent(event: UserScreenEvent) {
        when(event) {
            is UserScreenEvent.OnDeleteClick -> deleteUser(event.userEntity)
            is UserScreenEvent.OnInsertClick -> {
                insertUser(
                    userName = state.value.textNameValue,
                    password = state.value.textPasswordValue,
                )
                _state.update { it.copy(
                    selectedUserEntity = null,
                    textNameValue = "",
                    textPasswordValue = "",
                ) }
            }
            is UserScreenEvent.OnItemClick -> {
                _state.update { it.copy(
                    selectedUserEntity = event.userEntity
                ) }
            }
            is UserScreenEvent.OnNameTextChanged -> {
                _state.update { it.copy(
                    textNameError = null,
                    textNameValue = event.text,
                ) }
            }
            is UserScreenEvent.OnPasswordChanged -> {
                _state.update { it.copy(
                    textPasswordError = null,
                    textPasswordValue = event.text,
                ) }
            }
        }
    }

    private fun insertUser(userName: String, password: String) {
        if (userName == "root" && password == "toor") {
            _state.update { it.copy(
                textNameError = "Root can not be modified",
                textPasswordError = "Root can not be modified",
            ) }
            return
        }

        userName.ifEmpty {
            _state.update { it.copy(
                textNameError = "Empty field.",
            ) }
            return
        }

        password.ifEmpty {
            _state.update { it.copy(
                textPasswordError = "Empty field.",
            ) }
            return
        }

        val userEntity: UserEntity = if (state.value.selectedUserEntity == null) {
            UserEntity(
                userName = userName,
                password = password,
            )
        } else {
            UserEntity(
                userId = state.value.selectedUserEntity?.userId ?: UUID.randomUUID().toString(),
                userName = userName,
                password = password,
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insertUser(userEntity)
        }
    }

    private fun deleteUser(userEntity: UserEntity) {
        if (userEntity.userName == "root" && userEntity.password == "toor") {
            _state.update { it.copy(
                textNameError = "Root can not be modified",
                textPasswordError = "Root can not be modified",
            ) }
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.deleteUser(userEntity)
        }
    }

}