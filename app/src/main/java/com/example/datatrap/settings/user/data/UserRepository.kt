package com.example.datatrap.settings.user.data

import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    fun getUserEntityList(): Flow<List<UserEntity>> {
        return userDao.getUsers()
    }

    suspend fun insertUser(userEntity: UserEntity) {
        userDao.insertUser(userEntity)
    }

    suspend fun deleteUser(userEntity: UserEntity) {
        userDao.deleteUser(userEntity)
    }

    suspend fun getActiveUser(userId: String): UserEntity {
        return userDao.getActiveUser(userId)
    }

    suspend fun checkUser(userName: String, password: String): String? {
        return userDao.checkUser(userName, password)
    }

}