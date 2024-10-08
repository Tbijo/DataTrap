package com.example.datatrap.core.presentation.permission

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// Rationale Dialog

@Composable
fun PermissionDialog(
    // Which text to show in the dialog according to permission
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    // listener if user clicks outside the dialog
    onDismiss: () -> Unit,
    // listener if user clicks on OK to request a permission again after it was declined once
    onOkClick: () -> Unit,
    // listener if the user needs to navigate to the settings if he declined a permission twice
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Permission required")
        },
        text = {
            Text(
                text = permissionTextProvider.getDescription(
                    isPermanentlyDeclined = isPermanentlyDeclined
                )
            )
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Divider()

                Text(
                    text = if(isPermanentlyDeclined) {
                        // Declined twice go to settings
                        "Grant permission"
                    } else {
                        // When want to grant the permission again
                        "OK"
                    },
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isPermanentlyDeclined) {
                                onGoToAppSettingsClick()
                            } else {
                                onOkClick()
                            }
                        }
                        .padding(16.dp)
                )
            }
        }
    )
}

// More scalable way to provide messages about the state of a permission to a user
interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class ReadExtStoragePermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "It seems you permanently declined read external storage permission. " +
                    "You can go to the app settings to grant it."
        } else {
            "This app needs access to external storage " +
                    "to work properly."
        }
    }
}

class GPSPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "It seems you permanently declined GPS permission. " +
                    "You can go to the app settings to grant it."
        } else {
            "This app needs access to location " +
                    "to work properly."
        }
    }
}