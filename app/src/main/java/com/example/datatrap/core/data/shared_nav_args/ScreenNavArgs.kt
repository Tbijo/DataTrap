package com.example.datatrap.core.data.shared_nav_args

import android.content.SharedPreferences

private const val USER_ID_KEY = "userIdKey"
private const val TEAM_KEY = "teamKey"
private const val SYNC_DATE_KEY = "syncDateKey"

class ScreenNavArgs(
    private val sharedPreferences: SharedPreferences,
) {
    fun saveUserId(userId: String) {
        sharedPreferences.edit()
            .putString(USER_ID_KEY, userId)
            .apply()
    }

    fun readUserId(): String? {
        return sharedPreferences.getString(USER_ID_KEY, null)
    }

    fun saveUserTeam(selTeam: Int) {
        sharedPreferences.edit()
            .putInt(TEAM_KEY, selTeam)
            .apply()
    }

    fun readUserTeam(): Int {
        return sharedPreferences.getInt(TEAM_KEY, 0)
    }

    fun saveLastSyncDate(syncDate: String) {
        sharedPreferences.edit()
            .putString(SYNC_DATE_KEY, syncDate)
            .apply()
    }

    fun readLastSyncDate(): String? {
        return sharedPreferences.getString(SYNC_DATE_KEY, null)
    }
}