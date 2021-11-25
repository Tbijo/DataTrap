package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.User
import com.example.datatrap.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository
    val userList: LiveData<List<User>>
    var userId = MutableLiveData<Long>()

    init {
        val userDao = TrapDatabase.getDatabase(application).userDao()
        userRepository = UserRepository(userDao)
        userList = userRepository.userList
    }

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