package com.example.datatrap.sync.presentation

import java.time.ZonedDateTime

data class SyncUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val syncDateTime: ZonedDateTime = ZonedDateTime.now(),
)