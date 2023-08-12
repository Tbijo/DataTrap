package com.example.datatrap.settings.user.data

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {

    val userEntityList: LiveData<List<UserEntity>> = userDao.getUsers()

    suspend fun insertUser(userEntity: UserEntity) {
        userDao.insertUser(userEntity)
    }

    suspend fun updateUser(userEntity: UserEntity) {
        userDao.updateUser(userEntity)
    }

    suspend fun deleteUser(userEntity: UserEntity) {
        userDao.deleteUser(userEntity)
    }

    fun getActiveUser(userId: Long): LiveData<UserEntity> {
        return userDao.getActiveUser(userId)
    }

    suspend fun checkUser(userName: String, password: String): Long? {
        return userDao.checkUser(userName, password)
    }

}