package com.example.datatrap.settings.user.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Delete
    suspend fun deleteUser(userEntity: UserEntity)

    @Query("SELECT * FROM UserEntity")
    fun getUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM UserEntity WHERE userId = :userId")
    fun getActiveUser(userId: Long): Flow<UserEntity>

    @Query("SELECT userId FROM UserEntity WHERE userName = :userName AND password = :password")
    suspend fun checkUser(userName: String, password: String): Long?

}