package com.example.datatrap.core.data.pref

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

const val PATH_PREF_NAME = "my_pref"

class PrefRepository @Inject constructor(private val context: Context) {

    private object PreferencesKeys {
        val keyUserId = stringPreferencesKey("userId")
        val keyTeam = intPreferencesKey("team")
        val keySyncDate = longPreferencesKey("syncDate")
    }

    private val Context.storePref: DataStore<Preferences> by preferencesDataStore(name = PATH_PREF_NAME)

    suspend fun saveUserIdPref(userId: String) {
        context.storePref.edit { preference ->
            preference[PreferencesKeys.keyUserId] = userId
        }
    }

    suspend fun saveUserTeamPref(selTeam: Int) {
        context.storePref.edit { preference ->
            preference[PreferencesKeys.keyTeam] = selTeam
        }
    }

    suspend fun saveLastSyncDatePref(syncDate: Long) {
        context.storePref.edit { preference ->
            preference[PreferencesKeys.keySyncDate] = syncDate
        }
    }

    fun readUserIdPref(): Flow<String?> {
        return context.storePref.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.d("DataStore", exception.message.toString())
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preference ->
                val userId: String? = preference[PreferencesKeys.keyUserId]
                userId
            }
    }

    fun readUserTeamPref(): Flow<Int?> {
        return context.storePref.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.d("DataStore", exception.message.toString())
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preference ->
                val team: Int? = preference[PreferencesKeys.keyTeam]
                team
            }
    }

    fun readLastSyncDatePref(): Flow<Long?> {
        return context.storePref.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.d("DataStore", exception.message.toString())
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preference ->
                val lastSyncDate: Long? = preference[PreferencesKeys.keySyncDate]
                lastSyncDate
            }
    }
}