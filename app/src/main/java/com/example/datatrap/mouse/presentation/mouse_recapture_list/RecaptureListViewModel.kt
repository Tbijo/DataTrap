package com.example.datatrap.mouse.presentation.mouse_recapture_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.mouse.domain.use_case.GetMiceForRecapture
import com.example.datatrap.specie.data.SpecieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecaptureListViewModel(
    private val specieRepository: SpecieRepository,
    private val getMiceForRecapture: GetMiceForRecapture,
): ViewModel() {

    private val _state = MutableStateFlow(RecaptureListUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            specieRepository.getSpecies().collect { species ->
                _state.update { it.copy(
                    specieList = species,
                    isLoading = false,
                ) }
            }
        }
    }

    fun onEvent(event: RecaptureListScreenEvent) {
        when(event) {
            RecaptureListScreenEvent.OnSearchButtonClick -> {
                _state.update { it.copy(
                    isSheetExpanded = !state.value.isSheetExpanded,
                ) }
            }
            RecaptureListScreenEvent.OnConfirmClick -> {
                _state.update { it.copy(
                    isSheetExpanded = !state.value.isSheetExpanded,
                ) }
                searchForMice()
            }
            is RecaptureListScreenEvent.OnAgeClick -> {
                _state.update { it.copy(
                    ageState = event.age,
                ) }
            }
            is RecaptureListScreenEvent.OnCodeTextChanged -> {
                _state.update { it.copy(
                    codeText = event.text,
                ) }
            }
            is RecaptureListScreenEvent.OnGravidityClick -> {
                _state.update { it.copy(
                    gravidityState = event.gravidity,
                ) }
            }
            is RecaptureListScreenEvent.OnLactatingClick -> {
                _state.update { it.copy(
                    lactatingState = event.lactating,
                ) }
            }
            is RecaptureListScreenEvent.OnSelectFromDate -> {
                _state.update { it.copy(
                    fromDate = event.fromDate,
                ) }
            }
            is RecaptureListScreenEvent.OnSelectToDate -> {
                _state.update { it.copy(
                    toDate = event.toDate,
                ) }
            }
            is RecaptureListScreenEvent.OnSexActiveClick -> {
                _state.update { it.copy(
                    sexActiveState = event.sexActive,
                ) }
            }
            is RecaptureListScreenEvent.OnSexClick -> {
                _state.update { it.copy(
                    sexState = event.sex,
                ) }
            }
            RecaptureListScreenEvent.OnSpecieDismiss -> {
                _state.update { it.copy(
                    isSpecieDropDownExpanded = false,
                ) }
            }
            is RecaptureListScreenEvent.OnSpecieSelect -> {
                _state.update { it.copy(
                    selectedSpecie = event.specieEntity,
                ) }
            }

            RecaptureListScreenEvent.OnSpecieDropDownClick -> {
                _state.update { it.copy(
                    isSpecieDropDownExpanded = !state.value.isSpecieDropDownExpanded,
                ) }
            }

            else -> Unit
        }
    }

    private fun searchForMice() {
        viewModelScope.launch(Dispatchers.IO) {
            with(state.value) {
                getMiceForRecapture(
                    code = codeText.toIntOrNull(),
                    specieID = selectedSpecie?.specieId,
                    sex = sexState?.myName,
                    age = ageState?.myName,
                    gravidity = gravidityState,
                    sexActive = sexActiveState,
                    lactating = lactatingState,
                    dateFrom = fromDate,
                    dateTo = toDate,
                ).collect { list ->
                    _state.update { it.copy(
                        mouseList = list,
                    ) }
                }
            }
        }
    }
}