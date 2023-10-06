package com.example.datatrap.camera.presentation

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.camera.data.mouse_image.MouseImageEntity
import com.example.datatrap.camera.data.mouse_image.MouseImageRepository
import com.example.datatrap.camera.data.occasion_image.OccasionImageEntity
import com.example.datatrap.camera.data.occasion_image.OccasionImageRepository
import com.example.datatrap.camera.navigation.CameraScreens
import com.example.datatrap.core.data.storage.InternalStorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mouseImageRepository: MouseImageRepository,
    private val occasionImageRepository: OccasionImageRepository,
    private val internalStorageRepository: InternalStorageRepository,
) : ViewModel() {

    private val occasion = "Occasion"
    private val mouse = "Mouse"

    private val entity: String?
    private val id: String?

    private val _state = MutableStateFlow(CameraUiState())
    val state = _state.asStateFlow()

    init {
        entity = savedStateHandle.get<String>(CameraScreens.CameraScreen.entityTypeKey)
        id = savedStateHandle.get<String>(CameraScreens.CameraScreen.entityIdKey)

        viewModelScope.launch(Dispatchers.IO) {
            when(entity) {
                mouse -> {
                    id?.let {
                        val mouseImage = mouseImageRepository.getImageForMouse(id)
                        _state.update { it.copy(
                            mouseImageEntity = mouseImage,
                            imageName = mouseImage?.imgName,
                            note = mouseImage?.note,
                        ) }
                    }
                }
                occasion -> {
                    id?.let {
                        val occasionImage = occasionImageRepository.getImageForOccasion(id)
                        _state.update { it.copy(
                            occasionImageEntity = occasionImage,
                            imageName = occasionImage?.imgName,
                            note = occasionImage?.note,
                        ) }
                    }
                }
                else -> Unit
            }

            state.value.imageName?.let { imageName ->
                val image = internalStorageRepository.getImages().find {
                    it.name == imageName
                }
                _state.update { it.copy(
                    bitmap = image?.bmp,
                ) }
            }
        }
    }

    fun onEvent(event: CameraScreenEvent) {
        when(event) {
            CameraScreenEvent.OnInsertClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    when(entity) {
                        mouse -> {
                            insertMouseImage()
                        }
                        occasion -> {
                            insertOccasionImage()
                        }
                    }
                }
            }
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
        }
    }

    private suspend fun saveImageFile(bitmap: Bitmap) {

        val imageNamePart = when (entity) {
            mouse -> mouse
            occasion -> occasion
            else -> ""
        }
        val dateTime = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val newName = "${imageNamePart}_${dateTime}"

        val isImageSaved = internalStorageRepository.saveImage(
            fileName = newName,
            bmp = bitmap,
        )
        if (isImageSaved) {
            _state.update { it.copy(
                oldImageName = state.value.imageName,
                imageName = newName,
            ) }
        }
    }

    private suspend fun insertOccasionImage() {
        val currentOccasionImage = state.value.occasionImageEntity
        val imageName = state.value.imageName
        val note = state.value.note

        if (id == null) {
            _state.update { it.copy(
                error = "This should not happen.",
            ) }
            return
        }
        if (imageName == null) {
            _state.update { it.copy(
                error = "Error no image.",
            ) }
            return
        }

        val occasionImage = if (currentOccasionImage == null) {
            OccasionImageEntity(
                imgName = imageName,
                occasionID = id,
                note = note,
                dateTimeCreated = ZonedDateTime.now(),
                dateTimeUpdated = null,
            )
        }
        else {
            OccasionImageEntity(
                occasionImgId = currentOccasionImage.occasionImgId,
                imgName = imageName,
                occasionID = currentOccasionImage.occasionID,
                note = note,
                dateTimeCreated = currentOccasionImage.dateTimeCreated,
                dateTimeUpdated = ZonedDateTime.now(),
            )
        }

        occasionImageRepository.insertImage(occasionImage)

        // delete previous one if there was one
        // TODO walk through saving process with new image
        state.value.oldImageName?.let { imName ->
            internalStorageRepository.deleteImage(imName)
        }
    }

    private suspend fun insertMouseImage() {
        val currentMouseImage = state.value.mouseImageEntity
        val imageName = state.value.imageName
        val note = state.value.note

        if (id == null) {
            _state.update { it.copy(
                error = "This should not happen.",
            ) }
            return
        }

        if (imageName == null) {
            _state.update { it.copy(
                error = "No image.",
            ) }
            return
        }

        val mouseImageEntity = if (currentMouseImage == null) {
            MouseImageEntity(
                imgName = imageName,
                mouseID = id,
                note = note,
                dateTimeCreated = ZonedDateTime.now(),
                dateTimeUpdated = null,
            )
        } else {
            MouseImageEntity(
                mouseImgId = currentMouseImage.mouseImgId,
                imgName = imageName,
                mouseID = currentMouseImage.mouseID,
                note = note,
                dateTimeCreated = currentMouseImage.dateTimeCreated,
                dateTimeUpdated = ZonedDateTime.now(),
            )
        }

        mouseImageRepository.insertImage(mouseImageEntity)

        // delete previous one if there was one
        state.value.oldImageName?.let { imName ->
            internalStorageRepository.deleteImage(imName)
        }
    }
}