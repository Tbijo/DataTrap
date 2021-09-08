package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.UserDao
import com.example.datatrap.models.User

class UserRepository(private val userDao: UserDao) {

    val userList: LiveData<List<User>> = userDao.getUsers()

    suspend fun insertUser(user: User){
        userDao.insertUser(user)
    }

    suspend fun updateUser(user: User){
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User){
        userDao.deleteUser(user)
    }

    fun getActiveUser(): LiveData<User>{
        return userDao.getActiveUser()
    }

    fun getActiveUsers(): LiveData<List<User>>{
        return userDao.getActiveUsers()
    }

    fun checkUser(userName: String, password: String):LiveData<User>{
        return userDao.checkUser(userName, password)
    }
}