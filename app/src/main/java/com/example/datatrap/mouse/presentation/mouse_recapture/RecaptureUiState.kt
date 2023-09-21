package com.example.datatrap.mouse.presentation.mouse_recapture

import com.example.datatrap.mouse.data.MouseEntity

data class RecaptureUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val mouseEntity: MouseEntity? = null,
)
