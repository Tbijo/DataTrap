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

    fun getActiveUser(userId: Long): LiveData<User> {
        return userRepository.getActiveUser(userId)
    }

    fun checkUser(userName: String, password: String) {
        // ak volam nieco cez suspend a ukladam to do premennej MutableLiveData
        // cez .value nesmie sa pouzit Dispatchers.IO chyba: Can not use .value on Background Thread
        viewModelScope.launch {
            val value = userRepository.checkUser(userName, password)
            userId.value = value
        }
    }

}