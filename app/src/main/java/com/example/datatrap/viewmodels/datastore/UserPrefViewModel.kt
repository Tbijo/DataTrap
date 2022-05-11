package com.example.datatrap.viewmodels.datastore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.repositories.datastore.UserPrefRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPrefViewModel @Inject constructor(
    private val userPrefRepository: UserPrefRepository
) : ViewModel() {

    val readUserIdPref = userPrefRepository.readUserIdPref.asLiveData()

    val readUserTeamPref = userPrefRepository.readUserTeamPref.asLiveData()

    fun saveUserIdPref(idActiveUser: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            userPrefRepository.saveUserIdPref(idActiveUser)
        }
    }

    fun saveUserTeamPref(team: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            userPrefRepository.saveUserTeamPref(team)
        }
    }

}