package com.example.datatrap.specie.navigation

sealed class SpecieScreens(val route: String) {

    object SpecieListScreen: SpecieScreens("specie_list_screen")

    object SpecieScreen: SpecieScreens("specie_screen")

    object SpecieImageScreen: SpecieScreens("specie_image_screen")

    object SpecieDetailScreen: SpecieScreens("specie_detail_screen")

}