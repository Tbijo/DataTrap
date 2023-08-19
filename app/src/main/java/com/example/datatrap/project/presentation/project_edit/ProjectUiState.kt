package com.example.datatrap.project.presentation.project_edit

import com.example.datatrap.project.data.ProjectEntity

data class ProjectUiState(
    val selectedProject: ProjectEntity? = null,
    val isLoading: Boolean = true,
    val error: String = "",

    val projectName: String = "",
    val projectNameError: String? = null,

    val numLocal: String = "",
    val numLocalError: String? = null,

    val numMice: String = "",
    val numMiceError: String? = null,
)