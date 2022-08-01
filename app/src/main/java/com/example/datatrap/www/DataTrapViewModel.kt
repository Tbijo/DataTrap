package com.example.datatrap.www

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.models.sync.ImageSync
import com.example.datatrap.models.sync.SyncClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DataTrapViewModel @Inject constructor(
    private val repository: DataTrapRepository
) : ViewModel() {

    private val syncClassData = MutableLiveData<ResultWrapper<Response<List<SyncClass>>>>()
    // je vhodnejsie pouzit non mutable aby nebol pristup do value(setter) vo view
    val syncClassLiveData: LiveData<ResultWrapper<Response<List<SyncClass>>>> by this::syncClassData

    fun getData(unixTime: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            // pouzit postValue aby sa dal pouzit Dispatchers.IO
            val response = repository.getData(unixTime)
            syncClassData.postValue(response)
        }
    }

    private val dataInserted = MutableLiveData<ResultWrapper<Response<Void>>>()
    val dataInsertedLive: LiveData<ResultWrapper<Response<Void>>> by this::dataInserted

    fun insertData(dataList: List<SyncClass>) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.insertData(dataList)
            dataInserted.postValue(response)
        }
    }

    private val imageInserted = MutableLiveData<ResultWrapper<Response<Void>>>()
    val imageInsertedLive: LiveData<ResultWrapper<Response<Void>>> by this::imageInserted

    fun insertImageInfo(infoImages: ImageSync) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.insertImageInfo(infoImages)
            imageInserted.postValue(response)
        }
    }

    private val mouseFileInserted = MutableLiveData<ResultWrapper<Response<Void>>>()
    val mouseFileInsertedLive: LiveData<ResultWrapper<Response<Void>>> by this::mouseFileInserted

    fun insertMouseFiles(files: List<File>) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.insertMouseFiles(files)
            mouseFileInserted.postValue(response)
        }
    }

    private val occasionFileInserted = MutableLiveData<ResultWrapper<Response<Void>>>()
    val occasionFileInsertedLive: LiveData<ResultWrapper<Response<Void>>> by this::occasionFileInserted

    fun insertOccasionFiles(files: List<File>) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.insertOccasionFiles(files)
            occasionFileInserted.postValue(response)
        }
    }

    private val specieFileInserted = MutableLiveData<ResultWrapper<Response<Void>>>()
    val specieFileInsertedLive: LiveData<ResultWrapper<Response<Void>>> by this::specieFileInserted

    fun insertSpecieFiles(files: List<File>) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.insertSpecieFiles(files)
            specieFileInserted.postValue(response)
        }
    }

}