package com.example.datatrap.settings.navigation

sealed class SettingsScreens(val route: String) {

    object SettingsListScreen: SettingsScreens("settings_list_screen")

    object EnvListScreen: SettingsScreens("env_list_screen")

    object MethodListScreen: SettingsScreens("method_list_screen")

    object MethodTypeListScreen: SettingsScreens("method_type_list_screen")

    object TrapTypeListScreen: SettingsScreens("trap_type_list_screen")

    object VegTypeListScreen: SettingsScreens("veg_type_list_screen")

    object ProtocolListScreen: SettingsScreens("protocol_list_screen")

    object UserListScreen: SettingsScreens("user_list_screen")

}