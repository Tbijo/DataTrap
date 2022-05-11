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

const val PATH_PREF_NAME = "path_pref"

class PathPrefRepository @Inject constructor(private val context: Context) {

    private object PreferencesKeys {
        val keyPrjName = stringPreferencesKey("prjName")
        val keyLocName = stringPreferencesKey("locName")
        val keySesNum = intPreferencesKey("sesNum")
    }

    private val Context.pathStorePref: DataStore<Preferences> by preferencesDataStore(name = PATH_PREF_NAME)

    suspend fun savePrjNamePref(prjName: String) {
        context.pathStorePref.edit { preference ->
            preference[PreferencesKeys.keyPrjName] = prjName
        }
    }

    suspend fun saveLocNamePref(locName: String) {
        context.pathStorePref.edit { preference ->
            preference[PreferencesKeys.keyLocName] = locName
        }
    }

    suspend fun saveSesNumPref(sesNum: Int) {
        context.pathStorePref.edit { preference ->
            preference[PreferencesKeys.keySesNum] = sesNum
        }
    }

    val readPrjNamePref: Flow<String> = context.pathStorePref.data
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

    val readLocNamePref: Flow<String> = context.pathStorePref.data
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

    val readSesNumPref: Flow<Int> = context.pathStorePref.data
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
}