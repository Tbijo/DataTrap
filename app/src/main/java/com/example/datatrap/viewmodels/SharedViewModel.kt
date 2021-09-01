package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class SharedViewModel(application: Application): AndroidViewModel(application) {

    val dataToShare = MutableLiveData<String>()

    fun setData(data: String) {
        dataToShare.value = data
    }

}