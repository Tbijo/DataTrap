package com.example.datatrap.project.presentation.project_edit

import com.example.datatrap.project.data.ProjectEntity

data class ProjectUiState(
    val selectedProject: ProjectEntity? = null,
    val isLoading: Boolean = true,
    val error: String = "",
)