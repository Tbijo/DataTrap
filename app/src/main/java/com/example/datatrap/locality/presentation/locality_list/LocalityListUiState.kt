package com.example.datatrap.locality.presentation.locality_list

import com.example.datatrap.locality.data.LocalityEntity

data class LocalityListUiState(
    val localityList: List<LocalityEntity> = emptyList(),
    val isLoading: Boolean = true,
    val error: String = "",

    val projectName: String = "",

    val searchTextFieldValue: String = "",
    val searchTextFieldHint: String = "Enter locality name...",
    val isSearchTextFieldHintVisible: Boolean = true,
)
