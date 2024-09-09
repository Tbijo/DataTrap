package com.example.datatrap.specie.presentation.specie_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.NavigationScaffold
import com.example.datatrap.core.presentation.components.SearchTextField
import com.example.datatrap.specie.presentation.specie_list.components.SpecieListItem

@Composable
fun SpecieListScreen(
    onEvent: (SpecieListScreenEvent) -> Unit,
    state: SpecieListUiState,
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
    onEvent: (SpecieListScreenEvent) -> Unit,
    state: SpecieListUiState,
) {
    NavigationScaffold(
        title = "Species",
        errorState = state.error,
        onDrawerItemClick = {
            onEvent(
                SpecieListScreenEvent.OnDrawerItemClick(it)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(
                    SpecieListScreenEvent.OnAddButtonClick
                )
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add buton")
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            SearchTextField(
                text = state.searchTextFieldValue,
                hint = state.searchTextFieldHint,
                onValueChange = { text ->
                    onEvent(
                        SpecieListScreenEvent.OnSearchTextChange(text)
                    )
                },
                onFocusChange = { focusState ->
                    onEvent(
                        SpecieListScreenEvent.ChangeTitleFocus(focusState)
                    )
                },
                isHintVisible = state.isSearchTextFieldHintVisible,
            )
            LazyColumn {
                items(state.specieList) { specie ->
                    SpecieListItem(
                        specieEntity = specie,
                        onItemClick = { onEvent(SpecieListScreenEvent.OnItemClick(specie)) },
                        onDeleteClick = { onEvent(SpecieListScreenEvent.OnDeleteClick(specie)) },
                    )
                }
            }
        }
    }
}