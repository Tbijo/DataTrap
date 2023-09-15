package com.example.datatrap.mouse.presentation.mouse_add_multi

import com.example.datatrap.mouse.data.MouseEntity

data class MouseMultiUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val mouseList: List<MouseEntity>,
)
