package com.example.datatrap.core.presentation.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyBottomSheetScaffold(
    title: String,
    errorState: String?,
    isSheetExpanded: Boolean = false,
    floatingActionButton: @Composable () -> Unit = {},
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    sheetContent: @Composable ColumnScope.() -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed,
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState,
    )

    LaunchedEffect(key1 = isSheetExpanded) {
        if(sheetState.isCollapsed) {
            sheetState.expand()
        }
        else {
            sheetState.collapse()
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
        floatingActionButton = floatingActionButton,
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
                navigationIcon = navigationIcon,
                actions = actions,
            )
        },
        // normal content
        content = content,
    )
}

// sheetState.progress - current position