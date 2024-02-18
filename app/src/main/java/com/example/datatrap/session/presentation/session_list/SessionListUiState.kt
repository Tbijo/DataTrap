package com.example.datatrap.session.presentation.session_list

import com.example.datatrap.session.data.SessionEntity

data class SessionListUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val sessionList: List<SessionEntity> = emptyList(),

    val projectName: String = "",

    val localityName: String = "",
    val localityId: String = "",
)