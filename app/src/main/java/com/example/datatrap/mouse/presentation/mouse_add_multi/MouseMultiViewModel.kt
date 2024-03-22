package com.example.datatrap.mouse.presentation.mouse_add_multi

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.core.getMainScreenNavArgs
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.mouse.data.MouseEntity
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.mouse.domain.model.MultiMouse
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.specie.data.SpecieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class MouseMultiViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mouseRepository: MouseRepository,
    private val specieRepository: SpecieRepository,
    private val occasionRepository: OccasionRepository,
): ViewModel() {

    private val _state = MutableStateFlow(MouseMultiUiState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private lateinit var occasionId: String
    private lateinit var localityId: String

    init {
        viewModelScope.launch(Dispatchers.IO) {
            savedStateHandle.getMainScreenNavArgs()?.occasionId?.let {
                occasionId = it
            }
            savedStateHandle.getMainScreenNavArgs()?.localityId?.let {
                localityId = it
            }

            _state.update { it.copy(
                specieList = specieRepository.getNonSpecie(),
            ) }

            val occasion = occasionRepository.getOccasion(occasionId)

            _state.update { it.copy(
                trapIdList = (1..occasion.numTraps).toList(),
            ) }

            _state.update { it.copy(
                isLoading = false,
            ) }
        }
    }

    fun onEvent(event: MouseMultiScreenEvent) {
        when(event) {
            MouseMultiScreenEvent.OnInsertClick -> insertMultiMouse()

            MouseMultiScreenEvent.OnAddRowClick -> addRow()

            MouseMultiScreenEvent.OnRemoveRowClick -> removeRow()

            is MouseMultiScreenEvent.OnSpecieClick -> {
                val list = state.value.mouseList.toMutableList()
                val data = list[event.index]
                data.specie = event.specie
                list.add(event.index, data)

                _state.update { it.copy(
                    mouseList = list,
                ) }
            }
            is MouseMultiScreenEvent.OnSpecieDismissClick -> {
                val list = state.value.mouseList.toMutableList()
                val data = list[event.index]
                data.isSpecieExpanded = !data.isSpecieExpanded
                list.add(event.index, data)

                _state.update { it.copy(
                    mouseList = list,
                ) }
            }
            is MouseMultiScreenEvent.OnTrapIdClick -> {
                val list = state.value.mouseList.toMutableList()
                val data = list[event.index]
                data.trapID = event.trapId
                list.add(event.index, data)

                _state.update { it.copy(
                    mouseList = list,
                ) }
            }
            is MouseMultiScreenEvent.OnTrapIdDismissClick -> {
                val list = state.value.mouseList.toMutableList()
                val data = list[event.index]
                data.isTrapIdExpanded = !data.isTrapIdExpanded
                list.add(event.index, data)

                _state.update { it.copy(
                    mouseList = list,
                ) }
            }
        }
    }

    private fun addRow() {
        val list = state.value.mouseList.toMutableList()

        list.add(
            MultiMouse()
        )
        _state.update { it.copy(
            mouseList = list,
        ) }
    }

    private fun removeRow() {
        val list = state.value.mouseList.toMutableList()

        list.removeLastOrNull()
        _state.update { it.copy(
            mouseList = list,
        ) }
    }

    private fun insertMultiMouse() {
        val list = state.value.mouseList

        if (!checkInput(list)) return

        val mapped = list.map { mouse ->
            MouseEntity(
                code = null,
                primeMouseID = null,
                speciesID = mouse.specie?.specieId!!,
                occasionID = occasionId,
                localityID = localityId,
                protocolID = null,
                trapID = mouse.trapID,
                mouseDateTimeCreated = ZonedDateTime.now(),
                mouseDateTimeUpdated = null,
                age = null,
                sex = null,
                gravidity = null,
                lactating = null,
                sexActive = null,
                weight = null,
                recapture = null,
                captureID = null,
                body = null,
                tail = null,
                feet = null,
                ear = null,
                testesLength = null,
                testesWidth = null,
                embryoRight = null,
                embryoLeft = null,
                embryoDiameter = null,
                MC = null,
                MCright = null,
                MCleft = null,
                note = null,
                mouseCaught = ZonedDateTime.now(),
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            mouseRepository.insertMice(mapped)
            _eventFlow.emit(UiEvent.NavigateBack)
        }
    }

    private fun checkInput(list: List<MultiMouse>): Boolean {
        list.forEachIndexed { index, mouse ->
            if (mouse.specie == null) {
                _state.update { it.copy(
                    error = "SpecieID at position $index is empty.",
                ) }
                return false
            }
            if (mouse.trapID == 0) {
                _state.update { it.copy(
                    error = "TrapID at position $index is empty.",
                ) }
                return false
            }
        }
        return true
    }

}