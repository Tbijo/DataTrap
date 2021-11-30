package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.Occasion
import com.example.datatrap.models.tuples.OccList
import com.example.datatrap.models.tuples.OccasionView
import com.example.datatrap.repositories.OccasionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OccasionViewModel(application: Application): AndroidViewModel(application) {

    private val occasionRepository: OccasionRepository
    val occasionId: MutableLiveData<Long> = MutableLiveData<Long>()

    init {
        val occasionDao = TrapDatabase.getDatabase(application).occasionDao()
        occasionRepository = OccasionRepository(occasionDao)
    }

    fun insertOccasion(occasion: Occasion) {
        viewModelScope.launch {
            occasionId.value = occasionRepository.insertOccasion(occasion)
        }
    }

    fun updateOccasion(occasion: Occasion) {
        viewModelScope.launch(Dispatchers.IO) {
            occasionRepository.updateOccasion(occasion)
        }
    }

    fun deleteOccasion(occasionId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            occasionRepository.deleteOccasion(occasionId)
        }
    }

    fun getOccasion(occasionId: Long): LiveData<Occasion> {
        return occasionRepository.getOccasion(occasionId)
    }

    fun getOccasionView(occasionId: Long): LiveData<OccasionView> {
        return occasionRepository.getOccasionView(occasionId)
    }

    fun getOccasionsForSession(idSession: Long): LiveData<List<OccList>> {
        return occasionRepository.getOccasionsForSession(idSession)
    }

    fun getOccasionsForEmail(): LiveData<List<Occasion>> {
        return occasionRepository.getOccasionsForEmail()
    }
}