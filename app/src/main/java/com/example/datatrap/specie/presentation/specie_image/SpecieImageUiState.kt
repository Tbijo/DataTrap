package com.example.datatrap.specie.presentation.specie_image

import android.net.Uri
import com.example.datatrap.specie.data.specie_image.SpecieImageEntity

data class SpecieImageUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val specieImageEntity: SpecieImageEntity? = null,

    val imageUri: Uri? = null,

    val imageStateText: String = "",

    val note: String? = null,
)
