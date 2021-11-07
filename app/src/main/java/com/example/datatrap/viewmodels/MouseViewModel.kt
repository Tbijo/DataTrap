package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.Mouse
import com.example.datatrap.repositories.MouseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MouseViewModel(application: Application): AndroidViewModel(application) {

    private val mouseRepository: MouseRepository
    val gotMouse: MutableLiveData<Mouse> = MutableLiveData()

    init {
        val mouseDao = TrapDatabase.getDatabase(application).mouseDao()
        mouseRepository = MouseRepository(mouseDao)
    }

    fun insertMouse(mouse: Mouse){
        viewModelScope.launch(Dispatchers.IO) {
            mouseRepository.insertMouse(mouse)
        }
    }

    fun updateMouse(mouse: Mouse){
        viewModelScope.launch(Dispatchers.IO) {
            mouseRepository.updateMouse(mouse)
        }
    }

    fun deleteMouse(mouse: Mouse){
        viewModelScope.launch(Dispatchers.IO) {
            mouseRepository.deleteMouse(mouse)
        }
    }

    fun getMouse(idMouse: Long, deviceID: String) {
        viewModelScope.launch {
            val mouse = mouseRepository.getMouse(idMouse, deviceID)
            gotMouse.value = mouse
        }
    }

    fun getMiceForOccasion(idOccasion: Long): LiveData<List<Mouse>>{
        return mouseRepository.getMiceForOccasion(idOccasion)
    }

    fun getMiceForRecapture(code: Int): LiveData<List<Mouse>>{
        return mouseRepository.getMiceForRecapture(code)
    }

    fun getMiceForLog(idMouse: Long, deviceID: String): LiveData<List<Mouse>>{
        return mouseRepository.getMiceForLog(idMouse, deviceID)
    }

    fun getActiveMiceOfLocality(localityId: Long, currentTime: Long, twoYears: Long): LiveData<List<Mouse>>{
        return mouseRepository.getActiveMiceOfLocality(localityId, currentTime, twoYears)
    }

    fun countMiceForLocality(localityId: Long): LiveData<Int> {
        return mouseRepository.countMiceForLocality(localityId)

    }
}