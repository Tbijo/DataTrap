package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.Mouse
import com.example.datatrap.models.tuples.MouseLog
import com.example.datatrap.models.tuples.MouseOccList
import com.example.datatrap.models.tuples.MouseRecapList
import com.example.datatrap.models.tuples.MouseView
import com.example.datatrap.repositories.MouseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MouseViewModel(application: Application): AndroidViewModel(application) {

    private val mouseRepository: MouseRepository

    init {
        val mouseDao = TrapDatabase.getDatabase(application).mouseDao()
        mouseRepository = MouseRepository(mouseDao)
    }

    fun insertMouse(mouse: Mouse) {
        viewModelScope.launch(Dispatchers.IO) {
            mouseRepository.insertMouse(mouse)
        }
    }

    fun updateMouse(mouse: Mouse) {
        viewModelScope.launch(Dispatchers.IO) {
            mouseRepository.updateMouse(mouse)
        }
    }

    fun deleteMouse(mouseId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            mouseRepository.deleteMouse(mouseId)
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

    fun getMiceForRecapture(code: Int): LiveData<List<MouseRecapList>> {
        return mouseRepository.getMiceForRecapture(code)
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
}