package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.User

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

    @Query("SELECT * FROM User WHERE isActive = 1")
    fun getActiveUser(): LiveData<User>

    @Query("UPDATE User SET isActive = 0")
    suspend fun inactiveAllUsers()

    @Query("SELECT userId FROM User WHERE userName = :userName AND password = :password")
    suspend fun checkUser(userName: String, password: String): Long

    @Query("UPDATE User SET team = :team, isActive = 1 WHERE userId = :userId")
    suspend fun setActiveUser(team: Int, userId: Long)

    @Query("UPDATE User SET isActive = 0 WHERE isActive = 1")
    suspend fun inactivateUser()
}