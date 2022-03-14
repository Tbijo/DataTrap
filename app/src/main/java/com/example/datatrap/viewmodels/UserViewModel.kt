package com.example.datatrap.viewmodels

import androidx.lifecycle.*
import com.example.datatrap.models.User
import com.example.datatrap.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val userList: LiveData<List<User>> = userRepository.userList
    var userId = MutableLiveData<Long>()

    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insertUser(user)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.deleteUser(user)
        }
    }

    fun getActiveUser(): LiveData<User> {
        return userRepository.getActiveUser()
    }

    fun inactiveAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.inactiveAllUsers()
        }
    }

    fun checkUser(userName: String, password: String) {
        viewModelScope.launch {
            val value = userRepository.checkUser(userName, password)
            userId.value = value
        }
    }

    fun setActiveUser(team: Int, userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.setActiveUser(team, userId)
        }
    }

    fun inactivateUser() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.inactivateUser()
        }
    }
}