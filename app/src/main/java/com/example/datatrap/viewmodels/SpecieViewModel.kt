package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.Specie
import com.example.datatrap.models.tuples.SpecList
import com.example.datatrap.models.tuples.SpecSelectList
import com.example.datatrap.repositories.SpecieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SpecieViewModel(application: Application): AndroidViewModel(application) {

    private val specieRepository: SpecieRepository
    val specieList: LiveData<List<SpecList>>
    val specieId: MutableLiveData<Long> = MutableLiveData<Long>()

    init {
        val specieDao = TrapDatabase.getDatabase(application).specieDao()
        specieRepository = SpecieRepository(specieDao)
        specieList = specieRepository.specieList
    }

    fun insertSpecie(specie: Specie) {
        viewModelScope.launch {
            specieId.value = specieRepository.insertSpecie(specie)
        }
    }

    fun updateSpecie(specie: Specie) {
        viewModelScope.launch(Dispatchers.IO) {
            specieRepository.updateSpecie(specie)
        }
    }

    fun deleteSpecie(specieId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            specieRepository.deleteSpecie(specieId)
        }
    }

    fun getSpecie(specieId: Long): LiveData<Specie> {
        return specieRepository.getSpecie(specieId)
    }

    fun getSpeciesForSelect(): LiveData<List<SpecSelectList>> {
        return specieRepository.getSpeciesForSelect()
    }

    fun searchSpecies(specieCode: String): LiveData<List<SpecList>> {
        return specieRepository.searchSpecies(specieCode)
    }
}