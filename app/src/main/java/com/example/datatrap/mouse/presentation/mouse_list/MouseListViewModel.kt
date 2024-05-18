package com.example.datatrap.mouse.presentation.mouse_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.core.domain.use_case.GetInfoNamesUseCase
import com.example.datatrap.mouse.domain.use_case.DeleteMouseUseCase
import com.example.datatrap.mouse.domain.use_case.GetMiceByOccasion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MouseListViewModel(
    private val getMiceByOccasion: GetMiceByOccasion,
    private val deleteMouseUseCase: DeleteMouseUseCase,
    private val getInfoNamesUseCase: GetInfoNamesUseCase,
    private val projectId: String?,
    private val localityId: String?,
    private val sessionId: String?,
    private val occasionId: String?,
): ViewModel() {

    private val _state = MutableStateFlow(MouseListUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            occasionId?.let {
                getMiceByOccasion(occasionId).collect { mouseList ->
                    _state.update { it.copy(
                        mouseList = mouseList,
                    ) }
                }
            }

            projectId?.let {
                getInfoNamesUseCase(
                    projectId = projectId,
                    localityId = localityId,
                    sessionId = sessionId,
                    occasionId = occasionId,
                ).collect {
                    _state.update { it.copy(
                        projectName = it.projectName,
                        localityName = it.localityName,
                        sessionNum = it.sessionNum,
                        occasionNum = it.occasionNum,
                    ) }
                }
            }

            _state.update { it.copy(
                isLoading = false,
            ) }
        }
    }

    fun onEvent(event: MouseListScreenEvent) {
        when(event) {
            is MouseListScreenEvent.OnDeleteClick -> deleteMouse(event.mouseId)
            else -> Unit
        }
    }

    private fun deleteMouse(mouseId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteMouseUseCase(mouseId)
        }
    }

}