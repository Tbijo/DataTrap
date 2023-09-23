package com.example.datatrap.mouse.presentation.mouse_recapture_list

import com.example.datatrap.core.util.EnumMouseAge
import com.example.datatrap.core.util.EnumSex
import com.example.datatrap.mouse.domain.model.MouseRecapList
import com.example.datatrap.specie.data.SpecieEntity
import java.time.LocalDate

data class RecaptureListUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val mouseList: List<MouseRecapList> = emptyList(),

    val specieList: List<SpecieEntity> = emptyList(),
    val isSearchSectionVisible: Boolean = false,

    val codeText: String = "",
    val isSpecieDropDownExpanded: Boolean = false,
    val sexState: EnumSex? = null,
    val ageState: EnumMouseAge? = null,
    val sexActiveState: Boolean = false,
    val lactatingState: Boolean = false,
    val gravidityState: Boolean = false,
    val toDate: LocalDate = LocalDate.now(),
    val fromDate: LocalDate = LocalDate.now(),
    val selectedSpecie: SpecieEntity? = null,
)
