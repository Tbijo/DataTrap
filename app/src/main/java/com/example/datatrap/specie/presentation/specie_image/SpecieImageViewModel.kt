package com.example.datatrap.specie.presentation.specie_image

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val specieImageId = savedStateHandle.get<String>(SpecieScreens.SpecieImageScreen.specieIdKey)

            specieImageId?.let {
                val specieImage = specieImageRepository.getImageForSpecie(specieImageId)

                specieImage?.let {
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
            SpecieImageScreenEvent.OnGetImageClick -> {
                // check permissions
                // then getPicture() from gallery
            }
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
        }
    }

    private fun saveImage() {
        viewModelScope.launch(Dispatchers.IO) {
            // vytvara sa nova fotka, stara nebola
            val currentImageMouse = state.value.specieImageEntity
            if (currentImageMouse == null) {
                val specieImageEntity = SpecieImageEntity(
                    imgName = "imgName",
                    path = "imageUri",
                    note = state.value.note,
                    specieID = "",
                    dateTimeCreated = ZonedDateTime.now(),
                    dateTimeUpdated = null,
                )
                specieImageRepository.insertImage(specieImageEntity)

                // stara fotka existuje
            } else {

                // ostava stara fotka
                if ("imageName" == null) {
                    currentImageMouse.note = state.value.note
                    specieImageRepository.insertImage(currentImageMouse)

                    // nahradi sa stara fotka novou fotkou
                } else {

                    // vymazat zaznam starej fotky v databaze
                    specieImageRepository.deleteImage(currentImageMouse)

                    // pridat zaznam novej fotky do databazy subor uz existuje
                    val specieImageEntity = SpecieImageEntity(
                            imgName = "imageName",
                            path = "imageUri",
                            note = state.value.note,
                            specieID = "",
                            dateTimeCreated = ZonedDateTime.now(),
                            dateTimeUpdated = null,

                            )
                    specieImageRepository.insertImage(specieImageEntity)
                }
            }
        }
    }

}