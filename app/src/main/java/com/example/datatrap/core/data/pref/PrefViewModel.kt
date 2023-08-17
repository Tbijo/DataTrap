package com.example.datatrap.core.data.pref

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrefViewModel @Inject constructor(
    private val prefRepository: PrefRepository
) : ViewModel() {

    val readPrjNamePref = prefRepository.readPrjNamePref

    val readLocNamePref = prefRepository.readLocNamePref

    val readSesNumPref = prefRepository.readSesNumPref

    fun savePrjNamePref(prjName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            prefRepository.savePrjNamePref(prjName)
        }
    }

    fun saveLocNamePref(locName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            prefRepository.saveLocNamePref(locName)
        }
    }

    fun saveSesNumPref(sesNum: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            prefRepository.saveSesNumPref(sesNum)
        }
    }

    ////////////////////////////////USER////////////////////////////////////

    val readUserIdPref = prefRepository.readUserIdPref

    val readUserTeamPref = prefRepository.readUserTeamPref

    val readLastSyncDatePref = prefRepository.readLastSyncDatePref

    fun saveUserIdPref(idActiveUser: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            prefRepository.saveUserIdPref(idActiveUser)
        }
    }

    fun saveUserTeamPref(team: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            prefRepository.saveUserTeamPref(team)
        }
    }

    fun saveLastSyncDatePref(syncDate: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            prefRepository.saveLastSyncDatePref(syncDate)
        }
    }
}