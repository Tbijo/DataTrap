package com.example.datatrap.viewmodels

import androidx.lifecycle.*
import com.example.datatrap.models.Specie
import com.example.datatrap.models.tuples.SpecList
import com.example.datatrap.models.tuples.SpecSelectList
import com.example.datatrap.repositories.SpecieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SpecieViewModel @Inject constructor(
    private val specieRepository: SpecieRepository
): ViewModel() {

    val specieList: LiveData<List<SpecList>> = specieRepository.specieList
    val specieId: MutableLiveData<Long> = MutableLiveData<Long>()

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

    fun deleteSpecie(specieId: Long, imagePath: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val job1 = launch {
                if (imagePath != null) {
                    // odstranit fyzicku zlozku
                    val myFile = File(imagePath)
                    if (myFile.exists()) myFile.delete()
                }
            }
            val job2 = launch {
                specieRepository.deleteSpecie(specieId)
            }
            job1.join()
            job2.join()
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

    fun getNonSpecie(spCode: List<String>): LiveData<List<SpecList>> {
        return specieRepository.getNonSpecie(spCode)
    }
}