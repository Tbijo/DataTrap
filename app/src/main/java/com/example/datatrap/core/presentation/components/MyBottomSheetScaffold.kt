package com.example.datatrap.core.presentation.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottomSheetScaffold(
    title: String,
    errorState: String?,
    isSheetExpanded: Boolean = false,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    sheetContent: @Composable ColumnScope.() -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    LaunchedEffect(key1 = isSheetExpanded) {
        if(scaffoldState.bottomSheetState.isVisible) {
            scaffoldState.bottomSheetState.hide()
        }
        else {
            scaffoldState.bottomSheetState.expand()
        }
    }

    LaunchedEffect(key1 = errorState) {
        errorState?.let {
            scaffoldState.snackbarHostState.showSnackbar(
                message = it,
            )
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        // hidden content
        // height should not be full we maybe want to see something in the normal content (.height(300.dp))
        // or leave height out and it will occupy as much as it needs
        sheetContent = sheetContent,
        // hides sheet completely, now the button is necessary to show the sheet
        sheetPeekHeight = 0.dp,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = title)
                },
                navigationIcon = navigationIcon ?: {},
                actions = actions,
            )
        },
        // normal content
        content = content,
    )
}