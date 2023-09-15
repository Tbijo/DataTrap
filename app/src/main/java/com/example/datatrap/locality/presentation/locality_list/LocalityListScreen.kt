package com.example.datatrap.locality.presentation.locality_list

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.MyScaffold
import com.example.datatrap.core.presentation.components.SearchTextField
import com.example.datatrap.core.presentation.permission.LocalityPermissionTextProvider
import com.example.datatrap.core.presentation.permission.PermissionDialog
import com.example.datatrap.core.presentation.permission.hasFineLocationPermission
import com.example.datatrap.core.presentation.permission.openAppSettings
import com.example.datatrap.locality.presentation.locality_list.components.LocalityListItem

@Composable
fun LocalityListScreen(
    onEvent: (LocalityListScreenEvent) -> Unit,
    state: LocalityListUiState,
) {
    when(state.isLoading) {
        true -> LoadingScreen()
        false -> ScreenContent(
            onEvent = onEvent,
            state = state,
        )
    }
}

@Composable
private fun ScreenContent(
    onEvent: (LocalityListScreenEvent) -> Unit,
    state: LocalityListUiState,
) {
    val context = LocalContext.current

    val locationPermissionResultLauncher = rememberLauncherForActivityResult(
        // contract - which activity gets launched for what kind of result
        // ActivityResultContracts - is a list of all contracts
        contract = ActivityResultContracts.RequestPermission(),
        // onResult - gets called when the user selects grants or declines a permission
        onResult = { isGranted ->
            onEvent(
                LocalityListScreenEvent.OnPermissionResult(
                    permission = Manifest.permission.ACCESS_FINE_LOCATION,
                    isGranted = isGranted,
                )
            )
        }
    )

    LaunchedEffect(key1 = Unit) {
        locationPermissionResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    MyScaffold(
        title = "Locality List",
        errorState = state.error,
        actions = {
            IconButton(onClick = {
                if (context.hasFineLocationPermission()) {
                    onEvent(
                        LocalityListScreenEvent.OnMapButtonCLick
                    )
                } else {
                    locationPermissionResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }) {
                Icon(imageVector = Icons.Default.Map, contentDescription = "map icon")
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (context.hasFineLocationPermission()) {
                    onEvent(
                        LocalityListScreenEvent.OnAddButtonClick
                    )
                } else {
                    locationPermissionResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add icon")
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            SearchTextField(
                text = state.searchTextFieldValue,
                hint = state.searchTextFieldHint,
                onValueChange = { text ->
                    onEvent(
                        LocalityListScreenEvent.OnSearchTextChange(text)
                    )
                },
                onFocusChange = { text ->
                    onEvent(
                        LocalityListScreenEvent.ChangeTitleFocus(text)
                    )
                },
                isHintVisible = state.isSearchTextFieldHintVisible,
            )

            LazyColumn {
                item {
                    Text(text = "Project Name: ${state.projectName}")
                }

                items(state.localityList) {locality ->
                    LocalityListItem(
                        localityEntity = locality,
                        onItemClick = {
                            if (context.hasFineLocationPermission()) {
                                onEvent(
                                    LocalityListScreenEvent.OnItemClick(locality)
                                )
                            } else {
                                locationPermissionResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        },
                        onUpdateClick = {
                            if (context.hasFineLocationPermission()) {
                                onEvent(
                                    LocalityListScreenEvent.OnUpdateButtonClick(locality)
                                )
                            } else {
                                locationPermissionResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        },
                        onDeleteClick = {
                            onEvent(
                                LocalityListScreenEvent.OnDeleteClick(locality)
                            )
                        }
                    )
                }

            }
        }

        state.visiblePermissionDialogQueue.reversed().forEach { permission ->
            PermissionDialog(
                permissionTextProvider = when (permission) {
                    Manifest.permission.ACCESS_FINE_LOCATION -> {
                        LocalityPermissionTextProvider()
                    }
                    // a permission we did not want
                    else -> return@forEach
                },
                // Android does not support a way to find out whether a permission is permanently declined
                // If should not show Request Permission Rationale for a given permission we can be sure that the permission was permanently declined
                // it is not reliable because it will return false when we never requested the permission
                isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                    (context as Activity),
                    permission
                ),
                onDismiss = {
                    onEvent(LocalityListScreenEvent.OnDismissDialog)
                },
                onOkClick = {
                    // declined once
                    locationPermissionResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    onEvent(LocalityListScreenEvent.OnDismissDialog)
                },
                onGoToAppSettingsClick = {
                    // declined twice
                    (context as Activity).openAppSettings()
                    onEvent(LocalityListScreenEvent.OnDismissDialog)
                }
            )
        }
    }
}