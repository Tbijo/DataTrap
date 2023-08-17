package com.example.datatrap.project.presentation.project_edit

import android.app.AlertDialog
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.R
import com.example.datatrap.project.data.ProjectEntity
import com.example.datatrap.project.data.ProjectRepository
import com.example.datatrap.project.navigation.ProjectScreens
import com.example.datatrap.project.presentation.project_list.ProjectListUiState
import com.example.datatrap.project.presentation.project_list.ProjectListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _state = MutableStateFlow(ProjectUiState())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(
            isLoading = true,
        ) }
        savedStateHandle.get<String>(ProjectScreens.ProjectScreen.projectIdKey)?.let { id ->
            viewModelScope.launch {
                projectRepository.getProjectById(id).collect { project ->
                    _state.update { it.copy(
                        isLoading = false,
                        selectedProject = project,
                    ) }
                }
            }
        }

    }

    fun onEvent(event: ProjectScreenEvent) {
        when(event) {
            is ProjectScreenEvent.OnInsertClick -> updateProject()
        }

    }

    private fun checkInput(projectName: String, numLocal: String, numMouse: String): Boolean {
        return projectName.isNotEmpty() && numLocal.isNotEmpty() && numMouse.isNotEmpty()
    }
    private fun updateProject() {
        val projectName = binding.etProjectName.text.toString()
        val numLocal = binding.etNumLocality.text.toString()
        val numMice = binding.etNumMouse.text.toString()

        if (checkInput(projectName, numLocal, numMice)){
            val projectEntity: ProjectEntity = args.project.copy()
            projectEntity.projectName = projectName
            projectEntity.projectDateTimeUpdated = Calendar.getInstance().time
            projectEntity.numLocal = Integer.parseInt(numLocal)
            projectEntity.numMice = Integer.parseInt(numMice)

            viewModelScope.launch(Dispatchers.IO) {
                val projectEntity: ProjectEntity = ProjectEntity(name, Calendar.getInstance().time, null, 0, 0, Calendar.getInstance().time.time)
                projectRepository.insertProject(projectEntity)
            }

            Toast.makeText(requireContext(), "Project updated.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

}