package com.example.datatrap.core.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScaffold(
    title: String,
    errorState: String? = null,
    floatingActionButton: @Composable () -> Unit = {},
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    var myErrorState by remember {
        mutableStateOf(errorState)
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(myErrorState) {
        myErrorState?.let {
            snackbarHostState.showSnackbar(message = it)
            myErrorState = null
        }
    }

    Scaffold(
        floatingActionButton = floatingActionButton,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = title)
                },
                navigationIcon = navigationIcon ?: {},
                actions = actions,
            )
        },
        content = content,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    )
}