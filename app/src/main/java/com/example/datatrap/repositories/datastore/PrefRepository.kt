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

const val PATH_PREF_NAME = "my_pref"

class PrefRepository @Inject constructor(private val context: Context) {

    private object PreferencesKeys {
        val keyPrjName = stringPreferencesKey("prjName")
        val keyLocName = stringPreferencesKey("locName")
        val keySesNum = intPreferencesKey("sesNum")
        val keyUserId = longPreferencesKey("userId")
        val keyTeam = intPreferencesKey("team")
    }

    private val Context.storePref: DataStore<Preferences> by preferencesDataStore(name = PATH_PREF_NAME)

    suspend fun savePrjNamePref(prjName: String) {
        context.storePref.edit { preference ->
            preference[PreferencesKeys.keyPrjName] = prjName
        }
    }

    suspend fun saveLocNamePref(locName: String) {
        context.storePref.edit { preference ->
            preference[PreferencesKeys.keyLocName] = locName
        }
    }

    suspend fun saveSesNumPref(sesNum: Int) {
        context.storePref.edit { preference ->
            preference[PreferencesKeys.keySesNum] = sesNum
        }
    }

    val readPrjNamePref: Flow<String> = context.storePref.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("DataStore", exception.message.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val prjName: String = preference[PreferencesKeys.keyPrjName] ?: "none"
            prjName
        }

    val readLocNamePref: Flow<String> = context.storePref.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("DataStore", exception.message.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val locName: String = preference[PreferencesKeys.keyLocName] ?: "none"
            locName
        }

    val readSesNumPref: Flow<Int> = context.storePref.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("DataStore", exception.message.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val sesNum: Int = preference[PreferencesKeys.keySesNum] ?: 0
            sesNum
        }

    /////////////////////////////////USER//////////////////////////////////////////

    suspend fun saveUserIdPref(activeUserId: Long) {
        context.storePref.edit { preference ->
            preference[PreferencesKeys.keyUserId] = activeUserId
        }
    }

    suspend fun saveUserTeamPref(selTeam: Int) {
        context.storePref.edit { preference ->
            preference[PreferencesKeys.keyTeam] = selTeam
        }
    }

    val readUserIdPref: Flow<Long> = context.storePref.data
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

    val readUserTeamPref: Flow<Int> = context.storePref.data
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