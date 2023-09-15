package com.example.datatrap.occasion.presentation.occasion_list

import com.example.datatrap.occasion.data.occasion.OccasionEntity

data class OccasionListUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val occasionList: List<OccasionEntity> = emptyList(),

    val projectName: String = "",
    val localityName: String = "",
    val sessionNum: String = "",
)
