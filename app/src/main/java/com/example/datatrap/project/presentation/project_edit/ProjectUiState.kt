package com.example.datatrap.project.presentation.project_edit

data class ProjectUiState(
    val isLoading: Boolean = true,
    val error: String? = null,

    val projectName: String = "",
    val projectNameError: String? = null,

    val numLocal: String = "",
    val numLocalError: String? = null,

    val numMice: String = "",
    val numMiceError: String? = null,
)