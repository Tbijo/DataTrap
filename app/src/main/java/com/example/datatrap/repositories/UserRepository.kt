package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.UserDao
import com.example.datatrap.models.User

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

    fun getActiveUser(): LiveData<User> {
        return userDao.getActiveUser()
    }

    suspend fun inactiveAllUsers() {
        return userDao.inactiveAllUsers()
    }

    fun checkUser(userName: String, password: String): LiveData<Long> {
        return userDao.checkUser(userName, password)
    }

    suspend fun setActiveUser(team: Int, userId: Long) {
        userDao.setActiveUser(team, userId)
    }

    suspend fun inactivateUser() {
        userDao.inactivateUser()
    }
}