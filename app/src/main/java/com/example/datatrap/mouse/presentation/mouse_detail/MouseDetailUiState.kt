package com.example.datatrap.mouse.presentation.mouse_detail

import com.example.datatrap.mouse.data.MouseEntity

data class MouseDetailUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val mouseEntity: MouseEntity,
)
