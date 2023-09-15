package com.example.datatrap.mouse.presentation.mouse_list

import com.example.datatrap.mouse.data.MouseEntity

data class MouseListUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val mouseList: List<MouseEntity>,
)