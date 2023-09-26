package com.example.datatrap.specie.navigation

sealed class SpecieScreens(val route: String) {

    object SpecieListScreen: SpecieScreens("specie_list_screen")

    object SpecieScreen: SpecieScreens("specie_screen/{specieIdKey}") {
        const val specieIdKey = "specieIdKey"

        fun passParams(specieIdVal: String): String {
            return "occasion_screen/$specieIdVal"
        }
    }

    object SpecieImageScreen: SpecieScreens("specie_image_screen/{specieIdKey}") {
        const val specieIdKey = "specieIdKey"

        fun passParams(specieIdVal: String): String {
            return "specie_image_screen/$specieIdVal"
        }
    }

    object SpecieDetailScreen: SpecieScreens("specie_detail_screen/{specieIdKey}") {
        const val specieIdKey = "specieIdKey"

        fun passParams(specieIdVal: String): String {
            return "occasion_screen/$specieIdVal"
        }
    }

}