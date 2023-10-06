package com.example.datatrap.mouse.presentation.mouse_detail

import com.example.datatrap.mouse.domain.model.MouseView

data class MouseDetailUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val mouseView: MouseView? = null,

    val mouseImagePath: String? = null,
    val logList: List<String> = emptyList(),
    val isSheetExpanded: Boolean = false,
)
