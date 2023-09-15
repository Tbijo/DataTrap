package com.example.datatrap.specie.presentation.specie_list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.NavigationScaffold
import com.example.datatrap.specie.presentation.specie_list.components.SpecieListItem

@Composable
fun SpecieListScreen(
    onEvent: () -> Unit,
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
    onEvent: () -> Unit,
    state: SpecieListUiState,
) {
    val species = listOf("AAG")

    NavigationScaffold(
        title = "Species",
        errorState = state.error,
        onDrawerItemClick = {
            onEvent()
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(it)
        ) {
            items(species) { specie ->
                SpecieListItem(specieCode = specie, specieLatin = "Agrelus Akulus")
            }
        }
    }
}