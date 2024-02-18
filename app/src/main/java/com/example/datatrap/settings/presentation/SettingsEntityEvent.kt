package com.example.datatrap.settings.presentation

import com.example.datatrap.settings.data.SettingsEntity

sealed interface SettingsEntityEvent {
    object OnInsertClick: SettingsEntityEvent
    data class OnItemClick(val settingsEntity: SettingsEntity): SettingsEntityEvent
    data class OnDeleteClick(val settingsEntity: SettingsEntity): SettingsEntityEvent
    data class OnNameTextChanged(val text: String): SettingsEntityEvent
}