package com.example.datatrap.mouse.presentation.mouse_recapture_list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.MyBottomSheetScaffold
import com.example.datatrap.mouse.presentation.mouse_recapture_list.components.RecaptureListItem
import com.example.datatrap.mouse.presentation.mouse_recapture_list.components.RecaptureSearchHeader

@Composable
fun RecaptureListScreen(
    onEvent: (RecaptureListScreenEvent) -> Unit,
    state: RecaptureListUiState,
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
    onEvent: (RecaptureListScreenEvent) -> Unit,
    state: RecaptureListUiState,
) {
    MyBottomSheetScaffold(
        title = "Recapture List",
        errorState = state.error,
        isSheetExpanded = state.isSheetExpanded,
        sheetContent = {
            RecaptureSearchHeader(
                specieList = state.specieList,
                codeText = state.codeText,
                onCodeTextChanged = { text ->
                    onEvent(RecaptureListScreenEvent.OnCodeTextChanged(text))
                },
                isSpecieDropDownExpanded = state.isSpecieDropDownExpanded,
                onSpecieDropDownClick = {

                },
                onSpecieDismiss = { onEvent(RecaptureListScreenEvent.OnSpecieDismiss) },
                onSpecieSelect = { specie -> onEvent(RecaptureListScreenEvent.OnSpecieSelect(specie)) },
                onSexClick = { sex ->
                    onEvent(RecaptureListScreenEvent.OnSexClick(sex))
                },
                sexState = state.sexState,
                onAgeClick = { age ->
                    onEvent(RecaptureListScreenEvent.OnAgeClick(age))
                },
                ageState = state.ageState,
                onSexActiveClick = { sexActive ->
                    onEvent(RecaptureListScreenEvent.OnSexActiveClick(sexActive))
                },
                sexActiveState = state.sexActiveState,
                onLactatingClick = { lactating ->
                    onEvent(RecaptureListScreenEvent.OnLactatingClick(lactating))
                },
                lactatingState = state.lactatingState,
                onGravidityClick = { gravidity ->
                    onEvent(RecaptureListScreenEvent.OnGravidityClick(gravidity))
                },
                gravidityState = state.gravidityState,
                onSelectFromDate = { fromDate ->
                    onEvent(RecaptureListScreenEvent.OnSelectFromDate(fromDate))
                },
                onSelectToDate = { toDate ->
                    onEvent(RecaptureListScreenEvent.OnSelectToDate(toDate))
                },
                onConfirmClick = {
                    onEvent(RecaptureListScreenEvent.OnConfirmClick)
                },
            )
        },
        actions = {
            IconButton(onClick = {
                onEvent(RecaptureListScreenEvent.OnSearchButtonClick)
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "search icon")
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            items(state.mouseList) { mouse ->
                RecaptureListItem(
                    mouse = mouse,
                    onItemClick = {
                        onEvent(RecaptureListScreenEvent.OnItemClick(mouse))
                    },
                )
            }
        }
    }
}