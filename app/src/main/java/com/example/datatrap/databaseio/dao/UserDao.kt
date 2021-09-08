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
    fun getActiveUser(): LiveData<User>

    @Query("SELECT * FROM users WHERE isActive = 1")
    fun getActiveUsers(): LiveData<List<User>>

    @Query("SELECT * From users WHERE userName = :userName AND password = :password")
    fun checkUser(userName: String, password: String): LiveData<User>
}