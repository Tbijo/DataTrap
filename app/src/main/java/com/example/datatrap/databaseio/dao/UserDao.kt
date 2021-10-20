package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun initInsertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM users")
    fun getUsers(): LiveData<List<User>>

    @Query("SELECT * FROM users WHERE isActive = 1")
    suspend fun getActiveUser(): User?

    @Query("SELECT * FROM users WHERE isActive = 1")
    suspend fun getActiveUsers(): List<User>

    @Query("SELECT * From users WHERE userName = :userName AND password = :password")
    suspend fun checkUser(userName: String, password: String): User?
}