package com.example.datatrap.project.presentation.project_list

import com.example.datatrap.project.data.ProjectEntity

data class ProjectListUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val projectList: List<ProjectEntity> = emptyList(),

    val searchTextFieldValue: String = "",
    val searchTextFieldHint: String = "Enter project name...",
    val isSearchTextFieldHintVisible: Boolean = true,
)