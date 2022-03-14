package com.example.datatrap.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.models.Locality
import com.example.datatrap.models.tuples.LocList
import com.example.datatrap.repositories.LocalityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalityViewModel @Inject constructor (
    private val localityRepository: LocalityRepository
): ViewModel() {

    val localityList: LiveData<List<LocList>> = localityRepository.localityList

    fun insertLocality(locality: Locality) {
        viewModelScope.launch(Dispatchers.IO){
            localityRepository.insertLocality(locality)
        }
    }

    fun updateLocality(locality: Locality) {
        viewModelScope.launch(Dispatchers.IO){
            localityRepository.updateLocality(locality)
        }
    }

    fun deleteLocality(localityId: Long) {
        viewModelScope.launch(Dispatchers.IO){
            localityRepository.deleteLocality(localityId)
        }
    }

    fun getLocality(localityId: Long): LiveData<Locality> {
        return localityRepository.getLocality(localityId)
    }

    fun searchLocalities(localityName: String): LiveData<List<LocList>> {
        return localityRepository.searchLocalities(localityName)
    }
}