package com.example.datatrap.core.presentation.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun MyScaffold(
    title: String,
    errorState: String?,
    floatingActionButton: @Composable () -> Unit = {},
    drawerContent: @Composable (ColumnScope.() -> Unit)? = null,
    drawerGesturesEnabled: Boolean = true,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    content: @Composable (PaddingValues) -> Unit,
) {
    LaunchedEffect(key1 = errorState) {
        errorState?.let {
            scaffoldState.snackbarHostState.showSnackbar(
                message = it
            )
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = floatingActionButton,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = title)
                },
                navigationIcon = navigationIcon,
                actions = actions
            )
        },
        drawerGesturesEnabled = drawerGesturesEnabled,
        drawerContent = drawerContent,
        content = content,
    )
}