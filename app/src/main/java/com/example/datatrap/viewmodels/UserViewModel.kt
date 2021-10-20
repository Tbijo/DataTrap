package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.User
import com.example.datatrap.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserViewModel(application: Application) : AndroidViewModel(application) {

    val userList: LiveData<List<User>>
    private val userRepository: UserRepository

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

    fun getActiveUser(): User? {
        val user: User?
        runBlocking {
            user = userRepository.getActiveUser()
        }
        return user
    }

    fun getActiveUsers(): List<User> {
        var list: List<User>
        runBlocking {
            list = userRepository.getActiveUsers()
        }
        return list
    }

    fun checkUser(userName: String, password: String): User? {
        val user: User?
        runBlocking {
            user = userRepository.checkUser(userName, password)
        }
        return user
    }
}