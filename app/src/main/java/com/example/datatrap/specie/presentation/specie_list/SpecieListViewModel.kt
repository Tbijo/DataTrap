package com.example.datatrap.specie.presentation.specie_list

import androidx.lifecycle.*
import com.example.datatrap.specie.data.Specie
import com.example.datatrap.specie.data.SpecList
import com.example.datatrap.specie.data.SpecSelectList
import com.example.datatrap.specie.data.SpecieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SpecieListViewModel @Inject constructor(
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

fun onQueryTextChange(newText: String?): Boolean {
    if (newText != null) {
        searchForData(newText)
    }
    return true
}

private fun searchForData(query: String?) {
    val searchQuery = "%$query%"
    specieViewModel.searchSpecies(searchQuery).observe(viewLifecycleOwner) { species ->
        species.let {
            adapter.setData(it)
        }
    }
}

specieViewModel.specieList.observe(viewLifecycleOwner) { species ->
    adapter.setData(species)
}

binding.addSpecieFloatButton.setOnClickListener{
    val action = ListSpecieFragmentDirections.actionListSpecieFragmentToAddSpecieFragment()
    findNavController().navigate(action)
}

holder.binding.specieRow.setOnClickListener {
    // tu sa pojde pozriet specie
    val action = ListSpecieFragmentDirections.actionListSpecieFragmentToViewSpecieFragment(currenItem)
    holder.binding.root.findNavController().navigate(action)
}

holder.binding.specieRow.setOnLongClickListener {
    // tu sa pojde updatnut specie
    val action = ListSpecieFragmentDirections.actionListSpecieFragmentToUpdateSpecieFragment(currenItem)
    holder.binding.root.findNavController().navigate(action)
    true
}