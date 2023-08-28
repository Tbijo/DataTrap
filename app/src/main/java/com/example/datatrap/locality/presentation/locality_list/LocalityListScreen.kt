package com.example.datatrap.locality.presentation.locality_list

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.SearchTextField
import com.example.datatrap.core.presentation.permission.CameraPermissionTextProvider
import com.example.datatrap.core.presentation.permission.PermissionDialog
import com.example.datatrap.core.presentation.permission.PhoneCallPermissionTextProvider
import com.example.datatrap.core.presentation.permission.RecordAudioPermissionTextProvider
import com.example.datatrap.core.presentation.permission.openAppSettings
import com.example.datatrap.locality.presentation.locality_list.components.LocalityListItem

@Composable
fun LocalityListScreen(
    onEvent: (LocalityListScreenEvent) -> Unit,
    state: LocalityListUiState,
) {
    // one permission request
    val cameraPermissionResultLauncher = rememberLauncherForActivityResult(
        // contract - which activity gets launched for what kind of result
        // ActivityResultContracts - is a list of all contracts
        contract = ActivityResultContracts.RequestPermission(),
        // onResult - gets called when the user selects grants or declines a permission
        onResult = { isGranted ->
            viewModel.onPermissionResult(
                permission = Manifest.permission.CAMERA,
                isGranted = isGranted
            )
        }
        // This is just declaring a launcher we will launch it from a button
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Locality List")
                },
                actions = {
                    IconButton(onClick = {
                        // TODO Go to map
                        // hasPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                        // IF not permitted requested
                        cameraPermissionResultLauncher.launch(Manifest.permission.CAMERA)

                        onEvent(
                            LocalityListScreenEvent.OnMapButtonCLick
                        )
                    }) {
                        Icon(imageVector = Icons.Default.Map, contentDescription = "map icon")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(
                    LocalityListScreenEvent.OnAddButtonClick
                )
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add icon")
            }
        }
    ) {
        when(state.isLoading) {
            true -> LoadingScreen(paddingValues = it)
            false -> ScreenContent(
                paddingValues = it,
                onEvent = onEvent,
                state = state,
            )
        }
    }
}

@Composable
private fun ScreenContent(
    paddingValues: PaddingValues,
    onEvent: (LocalityListScreenEvent) -> Unit,
    state: LocalityListUiState,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues)
    ) {
        val dialogQueue = viewModel.visiblePermissionDialogQueue

        dialogQueue.forEach { permission ->
                PermissionDialog(
                    permissionTextProvider = when (permission) {
                        Manifest.permission.CAMERA -> {
                            CameraPermissionTextProvider()
                        }
                        // a permission with did not want
                        else -> return@forEach
                    },
                    // Android does not support a way to find out whether a permission is permanently declined
                    // If should not show Request Permission Rationale for a given permission we can be sure that the permission was permanently declined
                    // it is not reliable because it will return false when we never requested the permission
                    isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                        permission
                    ),
                    onDismiss = viewModel::dismissDialog,
                    onOkClick = {
                        viewModel.dismissDialog()
                        multiplePermissionResultLauncher.launch(
                            arrayOf(permission)
                        )
                    },
                    // Double decline go to settings
                    onGoToAppSettingsClick = {
                        val actitvity = context as Activity
                        actitvity.openAppSettings()
                    }
                )
            }
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
                        onEvent(
                            LocalityListScreenEvent.OnItemClick(locality)
                        )
                    },
                    onUpdateClick = {
                        onEvent(
                            LocalityListScreenEvent.OnUpdateButtonClick(locality)
                        )
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
}