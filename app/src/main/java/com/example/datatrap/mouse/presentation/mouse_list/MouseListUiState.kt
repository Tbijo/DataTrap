package com.example.datatrap.mouse.presentation.mouse_list

import com.example.datatrap.mouse.domain.model.MouseListData

data class MouseListUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val mouseList: List<MouseListData> = emptyList(),

    val projectName: String = "",
    val localityName: String = "",
    val sessionNum: Int = 0,
    val occasionNum: Int = 0,
)