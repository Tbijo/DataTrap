package com.example.datatrap.occasion.navigation

sealed class OccasionScreens(val route: String) {

    object OccasionListScreen: OccasionScreens("occasion_list_screen")

    object OccasionScreen: OccasionScreens("occasion_screen")

    object OccasionDetailScreen: OccasionScreens("occasion_detail_screen")

}