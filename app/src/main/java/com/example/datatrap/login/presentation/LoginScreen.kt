package com.example.datatrap.login.presentation

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.MyScaffold
import com.example.datatrap.core.presentation.components.MyTextField
import com.example.datatrap.core.presentation.components.ToggleButton
import com.example.datatrap.core.presentation.permission.GPSPermissionTextProvider
import com.example.datatrap.core.presentation.permission.PermissionDialog
import com.example.datatrap.core.presentation.permission.ReadExtStoragePermissionTextProvider
import com.example.datatrap.core.presentation.permission.hasFineLocationPermission
import com.example.datatrap.core.presentation.permission.hasReadExtStoragePermission
import com.example.datatrap.core.presentation.permission.openAppSettings
import com.example.datatrap.core.util.ScienceTeam

@Composable
fun LoginScreen(
    onEvent: (LoginScreenEvent) -> Unit,
    state: LoginUiState,
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
    onEvent: (LoginScreenEvent) -> Unit,
    state: LoginUiState,
) {
    val context = LocalContext.current
    val permissionsToRequest = buildList {
        add(Manifest.permission.ACCESS_FINE_LOCATION)
        // Same permission but different name according to API LEVEL
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            permissionsToRequest.forEach { permission ->
                onEvent(
                    LoginScreenEvent.OnPermissionResult(
                        permission = permission,
                        isGranted = perms[permission] == true,
                    )
                )
            }
        }
    )

    MyScaffold(
        title = "Log In",
        errorState = state.error
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LaunchedEffect(Unit) {
                multiplePermissionResultLauncher.launch(permissionsToRequest.toTypedArray())
            }

            MyTextField(
                value = state.userName,
                placeholder = "Palo",
                error = state.userNameError,
                label = "User Name",
                onValueChanged = { text ->
                    onEvent(
                        LoginScreenEvent.OnUserNameChanged(text)
                    )
                },
            )

            MyTextField(
                value = state.password,
                placeholder = "pali123",
                error = state.passwordError,
                label = "Password",
                onValueChanged = { text ->
                    onEvent(
                        LoginScreenEvent.OnPasswordChanged(text)
                    )
                },
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ToggleButton(text = ScienceTeam.EVEN_TEAM.label, isSelected = state.selectedTeam == ScienceTeam.EVEN_TEAM,
                    onClick = {
                        onEvent(
                            LoginScreenEvent.OnSelectTeam(ScienceTeam.EVEN_TEAM)
                        )
                    },
                )
                ToggleButton(text = ScienceTeam.ODD_TEAM.label, isSelected = state.selectedTeam == ScienceTeam.ODD_TEAM,
                    onClick = {
                        onEvent(
                            LoginScreenEvent.OnSelectTeam(ScienceTeam.ODD_TEAM)
                        )
                    },
                )
                ToggleButton(text = ScienceTeam.SINGLE.label, isSelected = state.selectedTeam == ScienceTeam.SINGLE,
                    onClick = {
                        onEvent(
                            LoginScreenEvent.OnSelectTeam(ScienceTeam.SINGLE)
                        )
                    },
                )
            }

            TextButton(onClick = {
                val gpsPermission = context.hasFineLocationPermission()
                val readExtStorage = context.hasReadExtStoragePermission()

                if (!gpsPermission || !readExtStorage) {
                    multiplePermissionResultLauncher.launch(
                        permissionsToRequest.toTypedArray()
                    )
                } else {
                    onEvent(
                        LoginScreenEvent.LogIn
                    )
                }

            }) {
                Text(text = "Log In")
            }

        }

        // We want the first dialog to be the last
        state.visiblePermissionDialogQueue.reversed().forEach { permission ->
            PermissionDialog(
                permissionTextProvider = when (permission) {
                    Manifest.permission.READ_MEDIA_IMAGES -> {
                        ReadExtStoragePermissionTextProvider()
                    }
                    Manifest.permission.READ_EXTERNAL_STORAGE -> {
                        ReadExtStoragePermissionTextProvider()
                    }
                    Manifest.permission.ACCESS_FINE_LOCATION -> {
                        GPSPermissionTextProvider()
                    }
                    // a permission we did not want
                    else -> return@forEach
                },
                // Android does not support a way to find out whether a permission is permanently declined
                // If should not show Request Permission Rationale for a given permission we can be sure that the permission was permanently declined
                // it is not reliable because it will return false when we never requested the permission
                isPermanentlyDeclined = !ActivityCompat.shouldShowRequestPermissionRationale(
                    (context as Activity),
                    permission
                ),
                onDismiss = {
                    onEvent(LoginScreenEvent.OnDismissDialog)
                },
                onOkClick = {
                    // declined once
                    onEvent(LoginScreenEvent.OnDismissDialog)
                    multiplePermissionResultLauncher.launch(
                        arrayOf(permission)
                    )
                },
                onGoToAppSettingsClick = {
                    // declined twice
                    (context as Activity).openAppSettings()
                    onEvent(LoginScreenEvent.OnDismissDialog)
                }
            )
        }
    }
}