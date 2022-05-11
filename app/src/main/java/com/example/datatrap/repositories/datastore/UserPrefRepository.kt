package com.example.datatrap.repositories.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

const val USER_PREF_NAME = "user_pref"

class UserPrefRepository @Inject constructor(private val context: Context) {

    private object PreferencesKeys {
        val keyUserId = longPreferencesKey("userId")
        val keyTeam = intPreferencesKey("team")
    }

    private val Context.userStorePref: DataStore<Preferences> by preferencesDataStore(name = USER_PREF_NAME)

    suspend fun saveUserIdPref(activeUserId: Long) {
        context.userStorePref.edit { preference ->
            preference[PreferencesKeys.keyUserId] = activeUserId
        }
    }

    suspend fun saveUserTeamPref(selTeam: Int) {
        context.userStorePref.edit { preference ->
            preference[PreferencesKeys.keyTeam] = selTeam
        }
    }

    val readUserIdPref: Flow<Long> = context.userStorePref.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("DataStore", exception.message.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val userId: Long = preference[PreferencesKeys.keyUserId] ?: 0L
            userId
        }

    val readUserTeamPref: Flow<Int> = context.userStorePref.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("DataStore", exception.message.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val team: Int = preference[PreferencesKeys.keyTeam] ?: -1
            team
        }

}