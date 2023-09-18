package com.example.datatrap.occasion.presentation.occasion_detail

import com.example.datatrap.occasion.data.occasion.OccasionEntity

data class OccasionDetailUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val occasionEntity: OccasionEntity? = null,

    val errorNum: Int = 0,
    val closeNum: Int = 0,
    val predatorNum: Int = 0,
    val pvpNum: Int = 0,
    val otherNum: Int = 0,
    val specieNum: Int = 0,

    val imagePath: String? = null,

    val localityName: String = "",

    val methodName: String = "",
    val methodTypeName: String = "",
    val trapTypeName: String = "",
    val envTypeName: String = "",
    val vegTypeName: String = "",
)
