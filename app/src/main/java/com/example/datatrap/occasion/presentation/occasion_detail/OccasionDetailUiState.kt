package com.example.datatrap.occasion.presentation.occasion_detail

import com.example.datatrap.occasion.data.occasion.OccasionEntity

data class OccasionDetailUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val occasionEntity: OccasionEntity? = null,
)
