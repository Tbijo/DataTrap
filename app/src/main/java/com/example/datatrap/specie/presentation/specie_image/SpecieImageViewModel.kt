package com.example.datatrap.specie.presentation.specie_image

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.core.util.ifNullOrBlank
import com.example.datatrap.specie.data.specie_image.SpecieImageEntity
import com.example.datatrap.specie.data.specie_image.SpecieImageRepository
import com.example.datatrap.specie.navigation.SpecieScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class SpecieImageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val specieImageRepository: SpecieImageRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SpecieImageUiState())
    val state = _state.asStateFlow()

    private var specieId: String? = null
    private var imageName: String? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            specieId = savedStateHandle.get<String>(SpecieScreens.SpecieImageScreen.specieIdKey)

            specieId?.let { specId ->
                val specieImage = specieImageRepository.getImageForSpecie(specId)

                specieImage?.let {
                    imageName = specieImage.imgName

                    _state.update { it.copy(
                        specieImageEntity = specieImage,
                        imageStateText = "Image added",
                        note = specieImage.note,
                    ) }

                } ?:
                _state.update { it.copy(
                    imageStateText = "No Image",
                ) }
            }
        }
    }

    fun onEvent(event: SpecieImageScreenEvent) {
        when(event) {
            SpecieImageScreenEvent.OnInsertClick -> {
                // ak je vsetko v poriadku treba
                // v pripade novej fotky treba staru fotku vymazat a ulozit novu fotku v databaze aj fyzicky
                if (state.value.specieImageEntity == null) {
                    _state.update { it.copy(
                        error = "No image was found.",
                    ) }
                } else {
                    saveImage()
                }
            }
            is SpecieImageScreenEvent.OnNoteTextChanged -> {
                _state.update { it.copy(
                    note = event.text,
                ) }
            }

            is SpecieImageScreenEvent.OnImageResult -> {
                _state.update { it.copy(
                    imageUri = event.uri,
                ) }
            }
        }
    }

    private fun saveImage() {
        val id = specieId ?: kotlin.run {
            _state.update { it.copy(
                error = "This should not happen",
            ) }
            return
        }
        val name = imageName.ifNullOrBlank {
            _state.update { it.copy(
                error = "No Image",
            ) }
            return
        }!!
        val uri = state.value.imageUri ?: kotlin.run {
            _state.update { it.copy(
                error = "No Image",
            ) }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            // vytvara sa nova fotka, stara nebola
            val currentImageMouse = state.value.specieImageEntity
            val specieImageEntity = if (currentImageMouse == null) {
                SpecieImageEntity(
                    imgName = name,
                    imageUri = uri,
                    note = state.value.note,
                    specieID = id,
                    dateTimeCreated = ZonedDateTime.now(),
                    dateTimeUpdated = null,
                )
            } else {
                // vymazat zaznam starej fotky v databaze
                specieImageRepository.deleteImage(currentImageMouse)

                // pridat zaznam novej fotky do databazy subor uz existuje
                SpecieImageEntity(
                    specieImgId = currentImageMouse.specieImgId,
                    imgName = name,
                    imageUri = uri,
                    note = state.value.note,
                    specieID = currentImageMouse.specieID,
                    dateTimeCreated = currentImageMouse.dateTimeCreated,
                    dateTimeUpdated = ZonedDateTime.now(),
                )
            }
            specieImageRepository.insertImage(specieImageEntity)
        }
    }

}