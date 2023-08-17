package com.example.datatrap.settings.user.data

import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    val userEntityList: Flow<List<UserEntity>> = userDao.getUsers()

    suspend fun insertUser(userEntity: UserEntity) {
        userDao.insertUser(userEntity)
    }

    suspend fun deleteUser(userEntity: UserEntity) {
        userDao.deleteUser(userEntity)
    }

    fun getActiveUser(userId: Long): Flow<UserEntity> {
        return userDao.getActiveUser(userId)
    }

    suspend fun checkUser(userName: String, password: String): Long? {
        return userDao.checkUser(userName, password)
    }

}