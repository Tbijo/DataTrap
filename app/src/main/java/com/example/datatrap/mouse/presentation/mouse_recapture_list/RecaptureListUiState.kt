package com.example.datatrap.mouse.presentation.mouse_recapture_list

import com.example.datatrap.mouse.data.MouseEntity

data class RecaptureListUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val mouseEntity: MouseEntity? = null,
)
