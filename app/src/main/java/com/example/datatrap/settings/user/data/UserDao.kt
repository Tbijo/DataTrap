package com.example.datatrap.settings.user.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM User")
    fun getUsers(): LiveData<List<User>>

    @Query("SELECT * FROM User WHERE userId = :userId")
    fun getActiveUser(userId: Long): LiveData<User>

    @Query("SELECT userId FROM User WHERE userName = :userName AND password = :password")
    suspend fun checkUser(userName: String, password: String): Long?

}