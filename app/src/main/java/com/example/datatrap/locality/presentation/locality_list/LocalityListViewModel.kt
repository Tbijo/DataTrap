package com.example.datatrap.locality.presentation.locality_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.locality.data.Locality
import com.example.datatrap.locality.data.LocList
import com.example.datatrap.locality.data.LocalityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalityListViewModel @Inject constructor (
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