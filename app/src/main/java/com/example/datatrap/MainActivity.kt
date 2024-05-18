package com.example.datatrap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.datatrap.about.aboutNavigation
import com.example.datatrap.camera.cameraNavigation
import com.example.datatrap.core.theme.DataTrapTheme
import com.example.datatrap.locality.localityNavigation
import com.example.datatrap.login.LoginScreenRoute
import com.example.datatrap.login.loginNavigation
import com.example.datatrap.mouse.mouseNavigation
import com.example.datatrap.occasion.occasionNavigation
import com.example.datatrap.project.projectNavigation
import com.example.datatrap.session.sessionNavigation
import com.example.datatrap.settings.settingsNavigation
import com.example.datatrap.specie.specieNavigation
import com.example.datatrap.sync.syncNavigation

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataTrapTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = LoginScreenRoute,
                ) {

                    loginNavigation(navController)

                    projectNavigation(navController)

                    localityNavigation(navController)

                    sessionNavigation(navController)

                    occasionNavigation(navController)

                    mouseNavigation(navController)

                    settingsNavigation(navController)

                    aboutNavigation(navController)

                    specieNavigation(navController)

                    syncNavigation(navController)

                    cameraNavigation(navController)
                }
            }
        }
    }

}