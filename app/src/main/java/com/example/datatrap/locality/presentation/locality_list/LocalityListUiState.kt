package com.example.datatrap.locality.presentation.locality_list

import com.example.datatrap.locality.data.locality.LocalityEntity

data class LocalityListUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val localityList: List<LocalityEntity> = emptyList(),

    val projectId: String? = null,
    val projectName: String = "",

    val searchTextFieldValue: String = "",
    val searchTextFieldHint: String = "Enter locality name...",
    val isSearchTextFieldHintVisible: Boolean = true,
)
