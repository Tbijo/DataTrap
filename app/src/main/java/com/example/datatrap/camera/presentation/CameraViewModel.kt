package com.example.datatrap.camera.presentation

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.camera.data.mouse_image.MouseImageRepository
import com.example.datatrap.camera.data.occasion_image.OccasionImageRepository
import com.example.datatrap.camera.util.EntityType
import com.example.datatrap.core.data.storage.InternalStorageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CameraViewModel(
    private val mouseImageRepository: MouseImageRepository,
    private val occasionImageRepository: OccasionImageRepository,
    private val internalStorageRepository: InternalStorageRepository,
    private val entity: EntityType?,
    private val entityId: String?,
) : ViewModel() {

    private val _state = MutableStateFlow(CameraUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            when(entity) {
                EntityType.MOUSE -> {
                    entityId?.let {
                        val mouseImage = mouseImageRepository.getImageForMouse(entityId)
                        val imageName = mouseImage?.imgName

                        _state.update { it.copy(
                            imageName = imageName,
                            note = mouseImage?.note,
                            contentDescription = "mouse image",
                            path = mouseImage?.path,
                        ) }
                    }
                }

                EntityType.OCCASION -> {
                    entityId?.let {
                        val occasionImage = occasionImageRepository.getImageForOccasion(entityId)
                        val imageName = occasionImage?.imgName

                        _state.update { it.copy(
                            imageName = imageName,
                            note = occasionImage?.note,
                            contentDescription = "occasion image",
                            path = occasionImage?.path,
                        ) }
                    }
                }

                else -> Unit
            }

            _state.update { it.copy(
                isLoading = false,
            ) }
        }
    }

    fun onEvent(event: CameraScreenEvent) {
        when(event) {
            is CameraScreenEvent.OnNoteTextChanged -> {
                _state.update { it.copy(
                    note = event.text,
                ) }
            }
            is CameraScreenEvent.OnImageReceived -> {
                viewModelScope.launch(Dispatchers.IO) {
                    saveImageFile(event.bitmap)
                }
            }

            CameraScreenEvent.OnDeleteImage -> deleteImageFile()

            else -> Unit
        }
    }

    private suspend fun saveImageFile(bitmap: Bitmap) {
        val imageNamePart = entity?.name ?: "N/A"

        val dateTime = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val newName = "${imageNamePart}_${dateTime}"

        val newImageFile = internalStorageRepository.saveImage(
            fileName = newName,
            bmp = bitmap,
        )

        _state.update { it.copy(
            path = newImageFile?.path,
            imageName = newImageFile?.name,
        ) }
    }

    private fun deleteImageFile() {
        viewModelScope.launch {
            state.value.imageName?.let { imageName ->
                internalStorageRepository.deleteImage(imageName)

                _state.update { it.copy(
                    imageName = null,
                    path = null,
                    note = null,
                ) }
            }
        }
    }
}