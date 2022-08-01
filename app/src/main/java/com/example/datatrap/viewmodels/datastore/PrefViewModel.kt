package com.example.datatrap.viewmodels.datastore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.repositories.datastore.PrefRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrefViewModel @Inject constructor(
    private val prefRepository: PrefRepository
) : ViewModel() {

    val readPrjNamePref = prefRepository.readPrjNamePref.asLiveData()

    val readLocNamePref = prefRepository.readLocNamePref.asLiveData()

    val readSesNumPref = prefRepository.readSesNumPref.asLiveData()

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

    val readUserIdPref = prefRepository.readUserIdPref.asLiveData()

    val readUserTeamPref = prefRepository.readUserTeamPref.asLiveData()

    val readLastSyncDatePref = prefRepository.readLastSyncDatePref.asLiveData()

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