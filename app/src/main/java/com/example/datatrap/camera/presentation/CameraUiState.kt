package com.example.datatrap.camera.presentation

data class CameraUiState(
    val isLoading: Boolean = true,
    val error: String? = null,

    val imageName: String? = null,
    val contentDescription: String = "",
    val path: String? = null,

    val note: String? = null,
)
