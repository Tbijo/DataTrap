package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.Specie
import com.example.datatrap.repositories.SpecieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SpecieViewModel(application: Application): AndroidViewModel(application) {

    val specieList: LiveData<List<Specie>>
    private val specieRepository: SpecieRepository

    init {
        val specieDao = TrapDatabase.getDatabase(application).specieDao()
        specieRepository = SpecieRepository(specieDao)
        specieList = specieRepository.specieList
    }

    fun insertSpecie(specie: Specie){
        viewModelScope.launch(Dispatchers.IO) {
            specieRepository.insertSpecie(specie)
        }
    }

    fun updateSpecie(specie: Specie){
        viewModelScope.launch(Dispatchers.IO) {
            specieRepository.updateSpecie(specie)
        }
    }

    fun deleteSpecie(specie: Specie){
        viewModelScope.launch(Dispatchers.IO) {
            specieRepository.deleteSpecie(specie)
        }
    }

    fun searchSpecies(specieCode: String): LiveData<List<Specie>>{
        return specieRepository.searchSpecies(specieCode)
    }
}