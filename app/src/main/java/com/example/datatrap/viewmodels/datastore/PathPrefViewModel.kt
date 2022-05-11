package com.example.datatrap.viewmodels.datastore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.repositories.datastore.PathPrefRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PathPrefViewModel @Inject constructor(
    private val pathPrefRepository: PathPrefRepository
) : ViewModel() {

    val readPrjNamePref = pathPrefRepository.readPrjNamePref.asLiveData()

    val readLocNamePref = pathPrefRepository.readLocNamePref.asLiveData()

    val readSesNumPref = pathPrefRepository.readSesNumPref.asLiveData()

    fun savePrjNamePref(prjName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            pathPrefRepository.savePrjNamePref(prjName)
        }
    }

    fun saveLocNamePref(locName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            pathPrefRepository.saveLocNamePref(locName)
        }
    }

    fun saveSesNumPref(sesNum: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            pathPrefRepository.saveSesNumPref(sesNum)
        }
    }
}