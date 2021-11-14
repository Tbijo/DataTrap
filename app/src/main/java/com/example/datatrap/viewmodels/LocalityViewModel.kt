package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.Locality
import com.example.datatrap.repositories.LocalityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LocalityViewModel(application: Application): AndroidViewModel(application) {

    val localityList: LiveData<List<Locality>>
    private val localityRepository: LocalityRepository

    init {
        val localityDao = TrapDatabase.getDatabase(application).localityDao()
        localityRepository = LocalityRepository(localityDao)
        localityList = localityRepository.localityList
    }

    fun insertLocality(locality: Locality){
        viewModelScope.launch(Dispatchers.IO){
            localityRepository.insertLocality(locality)
        }
    }

    fun updateLocality(locality: Locality){
        viewModelScope.launch(Dispatchers.IO){
            localityRepository.updateLocality(locality)
        }
    }

    fun deleteLocality(locality: Locality){
        viewModelScope.launch(Dispatchers.IO){
            localityRepository.deleteLocality(locality)
        }
    }

    fun getLocality(localityId: Long): Locality {
        val locality: Locality
        runBlocking {
            locality = localityRepository.getLocality(localityId)
        }
        return locality
    }

    fun searchLocalities(localityName: String): LiveData<List<Locality>> {
        return localityRepository.searchLocalities(localityName)
    }
}