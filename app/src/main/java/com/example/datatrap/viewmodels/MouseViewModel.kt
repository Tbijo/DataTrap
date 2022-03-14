package com.example.datatrap.viewmodels

import androidx.lifecycle.*
import com.example.datatrap.models.Mouse
import com.example.datatrap.models.tuples.MouseLog
import com.example.datatrap.models.tuples.MouseOccList
import com.example.datatrap.models.tuples.MouseRecapList
import com.example.datatrap.models.tuples.MouseView
import com.example.datatrap.repositories.MouseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MouseViewModel @Inject constructor(
    private val mouseRepository: MouseRepository
): ViewModel() {

    val mouseId: MutableLiveData<Long> = MutableLiveData<Long>()

    fun insertMouse(mouse: Mouse) {
        viewModelScope.launch {
            mouseId.value = mouseRepository.insertMouse(mouse)
        }
    }

    fun updateMouse(mouse: Mouse) {
        viewModelScope.launch(Dispatchers.IO) {
            mouseRepository.updateMouse(mouse)
        }
    }

    fun deleteMouse(mouseId: Long, imagePath: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val job1 = launch {
                if (imagePath != null) {
                    // odstranit fyzicku zlozku
                    val myFile = File(imagePath)
                    if (myFile.exists()) myFile.delete()
                }
            }
            val job2 = launch {
                mouseRepository.deleteMouse(mouseId)
            }
            job1.join()
            job2.join()
        }
    }

    fun getMouse(mouseId: Long): LiveData<Mouse> {
        return mouseRepository.getMouse(mouseId)
    }

    fun getMouseForView(idMouse: Long, deviceID: String): LiveData<MouseView> {
        return mouseRepository.getMouseForView(idMouse, deviceID)
    }

    fun getMiceForOccasion(idOccasion: Long): LiveData<List<MouseOccList>> {
        return mouseRepository.getMiceForOccasion(idOccasion)
    }

    fun getMiceForRecapture(code: Int, currentTime: Long, twoYears: Long): LiveData<List<MouseRecapList>> {
        return mouseRepository.getMiceForRecapture(code, currentTime, twoYears)
    }

    fun getMiceForLog(primeMouseID: Long, deviceID: String): LiveData<List<MouseLog>> {
        return mouseRepository.getMiceForLog(primeMouseID, deviceID)
    }

    fun getActiveCodeOfLocality(localityId: Long, currentTime: Long, twoYears: Long): LiveData<List<Int>> {
        return mouseRepository.getActiveCodeOfLocality(localityId, currentTime, twoYears)
    }

    fun countMiceForLocality(localityId: Long): LiveData<Int> {
        return mouseRepository.countMiceForLocality(localityId)
    }

    fun getMiceForEmail(): LiveData<List<Mouse>> {
        return mouseRepository.getMiceForEmail()
    }
}