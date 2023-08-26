package com.example.datatrap.session.presentation.session_edit

import com.example.datatrap.session.data.SessionEntity

data class SessionUiState(
    val session: SessionEntity? = null,
    val isLoading: Boolean = true,
    val error: String? = null,

    val sessionNum: String = "",
    val sessionNumError: String? = null,

    val numOcc: String = "",
    val numOccError: String? = null,
)