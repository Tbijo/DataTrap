package com.example.datatrap.session.presentation.session_list

import com.example.datatrap.session.data.SessionEntity

sealed interface SessionListScreenEvent {
    data class OnItemClick(val sessionEntity: SessionEntity, val localityId: String): SessionListScreenEvent
    data class OnUpdateButtonClick(val sessionEntity: SessionEntity): SessionListScreenEvent

    data class OnDeleteClick(val sessionEntity: SessionEntity): SessionListScreenEvent

    object OnAddButtonClick: SessionListScreenEvent

    data class SetSesNumInLocality(val sessionId: String): SessionListScreenEvent
}