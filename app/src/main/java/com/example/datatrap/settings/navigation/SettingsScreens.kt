package com.example.datatrap.settings.navigation

sealed class SettingsScreens(val route: String) {

    object SettingsListScreen: SettingsScreens("settings_list_screen")

}