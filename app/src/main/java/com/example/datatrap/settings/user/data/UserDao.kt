package com.example.datatrap.settings.user.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Update
    suspend fun updateUser(userEntity: UserEntity)

    @Delete
    suspend fun deleteUser(userEntity: UserEntity)

    @Query("SELECT * FROM UserEntity")
    fun getUsers(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM UserEntity WHERE userId = :userId")
    fun getActiveUser(userId: Long): LiveData<UserEntity>

    @Query("SELECT userId FROM UserEntity WHERE userName = :userName AND password = :password")
    suspend fun checkUser(userName: String, password: String): Long?

}