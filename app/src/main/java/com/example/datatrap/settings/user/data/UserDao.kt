package com.example.datatrap.settings.user.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
    suspend fun getActiveUser(userId: String): UserEntity

    @Query("SELECT userId FROM UserEntity WHERE userName = :userName AND password = :password")
    suspend fun checkUser(userName: String, password: String): String?

}