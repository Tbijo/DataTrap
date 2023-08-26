package com.example.datatrap.session.presentation.session_list

import com.example.datatrap.session.data.SessionEntity

data class SessionListUiState(
    val sessionList: List<SessionEntity> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val projectName: String = "",
    val projectId: String = "",
    val localityName: String = "",
    val localityId: String = "",
)