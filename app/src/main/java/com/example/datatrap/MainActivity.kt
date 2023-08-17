package com.example.datatrap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.datatrap.about.navigation.aboutNavigation
import com.example.datatrap.camera.navigation.cameraNavigation
import com.example.datatrap.core.theme.DataTrapTheme
import com.example.datatrap.locality.navigation.localityNavigation
import com.example.datatrap.login.navigation.LoginScreens
import com.example.datatrap.login.navigation.loginNavigation
import com.example.datatrap.mouse.navigation.mouseNavigation
import com.example.datatrap.occasion.navigation.occasionNavigation
import com.example.datatrap.project.navigation.projectNavigation
import com.example.datatrap.session.navigation.sessionNavigation
import com.example.datatrap.settings.navigation.settingsNavigation
import com.example.datatrap.specie.navigation.specieNavigation
import com.example.datatrap.sync.navigation.syncNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataTrapTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = LoginScreens.LoginScreen.route
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