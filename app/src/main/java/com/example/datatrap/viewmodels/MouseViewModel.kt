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
import kotlinx.coroutines.runBlocking

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

    fun getMouse(idMouse: Long) {
        viewModelScope.launch {
            val mouse = mouseRepository.getMouse(idMouse)
            gotMouse.value = mouse
        }
    }

    fun getMiceForOccasion(idOccasion: Long): LiveData<List<Mouse>>{
        return mouseRepository.getMiceForOccasion(idOccasion)
    }

    fun getMiceForRecapture(code: Int): LiveData<List<Mouse>>{
        return mouseRepository.getMiceForRecapture(code)
    }

    fun getMiceForLog(idMouse: Long): LiveData<List<Mouse>>{
        return mouseRepository.getMiceForLog(idMouse)
    }

    fun getActiveMiceOfLocality(localityId: Long, currentTime: Long, twoYears: Long): LiveData<List<Mouse>>{
        return mouseRepository.getActiveMiceOfLocality(localityId, currentTime, twoYears)
    }

    fun countMiceForLocality(localityId: Long): Int {
        val count: Int
        runBlocking {
            count = mouseRepository.countMiceForLocality(localityId)
        }
        return count
    }
}