package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.Mouse
import com.example.datatrap.repositories.MouseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MouseViewModel(application: Application): AndroidViewModel(application) {

    private val mouseRepository: MouseRepository

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

    fun getMiceForOccasion(idOccasion: Int): Flow<List<Mouse>>{
        return mouseRepository.getMiceForOccasion(idOccasion)
    }
}