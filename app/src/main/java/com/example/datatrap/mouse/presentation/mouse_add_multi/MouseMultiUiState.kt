package com.example.datatrap.mouse.presentation.mouse_add_multi

import com.example.datatrap.mouse.domain.model.MultiMouse
import com.example.datatrap.specie.domain.model.SpecList

data class MouseMultiUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val mouseList: List<MultiMouse> = emptyList(),

    val trapIdList: List<Int> = emptyList(),
    val specieList: List<SpecList> = emptyList(),
)
