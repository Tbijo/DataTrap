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

    fun getActiveUser(userId: String): Flow<UserEntity> {
        return userDao.getActiveUser(userId)
    }

    fun checkUser(userName: String, password: String): Flow<String?> {
        return userDao.checkUser(userName, password)
    }

}