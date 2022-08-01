package com.example.datatrap.viewmodels

import androidx.lifecycle.*
import com.example.datatrap.models.Occasion
import com.example.datatrap.models.sync.OccasionSync
import com.example.datatrap.models.tuples.OccList
import com.example.datatrap.models.tuples.OccasionView
import com.example.datatrap.repositories.OccasionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class OccasionViewModel @Inject constructor(
    private val occasionRepository: OccasionRepository
): ViewModel() {

    val occasionId: MutableLiveData<Long> = MutableLiveData<Long>()

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

    fun deleteOccasion(occasionId: Long, imagePath: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val job1 = launch {
                if (imagePath != null) {
                    // odstranit fyzicku zlozku
                    val myFile = File(imagePath)
                    if (myFile.exists()) myFile.delete()
                }
            }
            val job2 = launch {
                occasionRepository.deleteOccasion(occasionId)
            }
            job1.join()
            job2.join()
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

}