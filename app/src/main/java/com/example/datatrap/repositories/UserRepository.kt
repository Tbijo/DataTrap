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

    suspend fun getActiveUser(): User? {
        return userDao.getActiveUser()
    }

    suspend fun getActiveUsers(): List<User>{
        return userDao.getActiveUsers()
    }

    suspend fun checkUser(userName: String, password: String): User? {
        return userDao.checkUser(userName, password)
    }
}