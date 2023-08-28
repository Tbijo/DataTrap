package com.example.datatrap.core.presentation.permission

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        // package name of our application to show the detail settings
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}