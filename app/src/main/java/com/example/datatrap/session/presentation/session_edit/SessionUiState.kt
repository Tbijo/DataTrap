package com.example.datatrap.session.presentation.session_edit

data class SessionUiState(
    val isLoading: Boolean = true,
    val error: String? = null,

    val sessionNum: String = "",
    val sessionNumError: String? = null,

    val numOcc: String = "",
    val numOccError: String? = null,
)