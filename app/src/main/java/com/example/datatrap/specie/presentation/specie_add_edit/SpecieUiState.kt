package com.example.datatrap.specie.presentation.specie_add_edit

import android.net.Uri

data class SpecieUiState(
    val isLoading: Boolean = true,
    val error: String? = null,

    val specieCode: String = "",
    val specieCodeError: String? = null,
    val fullName: String = "",
    val fullNameError: String? = null,
    val synonym: String = "",
    val synonymError: String? = null,
    val authority: String = "",
    val authorityError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,

    val isSmall: Boolean = false,
    val numOfFingers: Int? = null,

    val minWeight: String = "",
    val maxWeight: String = "",
    val bodyLength: String = "",
    val tailLength: String = "",
    val minFeetLength: String = "",
    val maxFeetLength: String = "",
    val note: String = "",

    val imageId: String? = null,
    val imageUri: Uri? = null,
    val imageNote: String? = null,
)
