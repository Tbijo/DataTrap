package com.example.datatrap.locality.presentation.locality_map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.MyScaffold
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocalityMapScreen(
    state: LocalityMapUiState,
) {
    when(state.isLoading) {
        true -> LoadingScreen()
        false -> ScreenContent(
            state = state,
        )
    }
}

@Composable
private fun ScreenContent(
    state: LocalityMapUiState,
) {
    MyScaffold(
        title = "Map",
        errorState = state.error,
    ) {
        val cameraPositionState = rememberCameraPositionState {
            if (state.localityList.isNotEmpty()) {
                val lastLat = state.localityList.last().xA
                val lastlon = state.localityList.last().yA
                if (lastLat != null && lastlon != null) {
                    val lastLatLon = LatLng(lastLat.toDouble(), lastlon.toDouble())
                    // set to newest position
                    position = CameraPosition.fromLatLngZoom(lastLatLon, 10f)
                }
            }
        }

        var uiSettings by remember { mutableStateOf(MapUiSettings()) }
        var properties by remember {
            mutableStateOf(MapProperties(mapType = MapType.SATELLITE))
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            GoogleMap(
                cameraPositionState = cameraPositionState,
                properties = properties,
                uiSettings = uiSettings,
            ) {
                state.localityList.forEach { localEnt ->
                    val latitudeA = localEnt.xA
                    val longitudeA = localEnt.yA

                    if (latitudeA != null && longitudeA != null) {
                        val lastLatLon = LatLng(latitudeA.toDouble(), longitudeA.toDouble())
                        Marker(
                            state = MarkerState(position = lastLatLon),
                            title = localEnt.localityName,
                            snippet = localEnt.note
                        )
                    }
                }
            }

            Switch(
                checked = uiSettings.zoomControlsEnabled,
                onCheckedChange = { checkChanged ->
                    uiSettings = uiSettings.copy(zoomControlsEnabled = checkChanged)
                }
            )
        }
    }
}