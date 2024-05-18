package com.example.datatrap.mouse.presentation.mouse_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.camera.data.mouse_image.MouseImageRepository
import com.example.datatrap.mouse.domain.use_case.GetMouseDetail
import com.example.datatrap.mouse.domain.use_case.GetPreviousLogsOfMouse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MouseDetailViewModel(
    private val mouseImageRepository: MouseImageRepository,
    private val getMouseDetail: GetMouseDetail,
    private val getPreviousLogsOfMouse: GetPreviousLogsOfMouse,
    private val mouseId: String?,
): ViewModel() {

    private val _state = MutableStateFlow(MouseDetailUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mouseId?.let {
                getMouseDetail(mouseId).collect { mouseView ->
                    _state.update { it.copy(
                        mouseView = mouseView,
                    ) }

                    val id = mouseView.primeMouseId ?: mouseView.mouseId

                    getPreviousLogsOfMouse(id).collect { mouseLogList ->
                        _state.update { it.copy(
                            logList = mouseLogList,
                        ) }
                    }
                }

                _state.update { it.copy(
                    mouseImagePath = mouseImageRepository.getImageForMouse(mouseId)?.path,
                ) }
            }

            _state.update { it.copy(
                isLoading = false,
            ) }
        }
    }

    fun onEvent(event: MouseDetailScreenEvent) {
        when(event) {
            MouseDetailScreenEvent.OnImageClick -> {
                _state.update { it.copy(
                    isSheetExpanded = !state.value.isSheetExpanded,
                ) }
            }
        }
    }
}