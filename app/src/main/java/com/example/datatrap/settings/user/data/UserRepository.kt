package com.example.datatrap.settings.user.data

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {

    val userList: LiveData<List<User>> = userDao.getUsers()

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    fun getActiveUser(userId: Long): LiveData<User> {
        return userDao.getActiveUser(userId)
    }

    suspend fun checkUser(userName: String, password: String): Long? {
        return userDao.checkUser(userName, password)
    }

}