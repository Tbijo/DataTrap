package com.example.datatrap.session.presentation.session_edit

import com.example.datatrap.session.data.SessionEntity

data class SessionUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val session: SessionEntity? = null,

    val sessionNum: String = "",
    val sessionNumError: String? = null,

    val numOcc: String = "",
    val numOccError: String? = null,
)