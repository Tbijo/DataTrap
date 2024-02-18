package com.example.datatrap.mouse.presentation.mouse_add_multi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.MyScaffold
import com.example.datatrap.mouse.presentation.mouse_add_multi.components.MouseMultiItem

@Composable
fun MouseMultiScreen(
    onEvent: (MouseMultiScreenEvent) -> Unit,
    state: MouseMultiUiState,
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
    onEvent: (MouseMultiScreenEvent) -> Unit,
    state: MouseMultiUiState,
) {
    MyScaffold(
        title = "Mouse Multi",
        errorState = state.error,
        actions = {
            IconButton(onClick = {
                onEvent(MouseMultiScreenEvent.OnInsertClick)
            }) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "save mouse")
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            Row {
                Button(onClick = {
                    onEvent(MouseMultiScreenEvent.OnAddRowClick)
                }) {
                    Text(text = "Add Row")
                }
                Button(onClick = {
                    onEvent(MouseMultiScreenEvent.OnRemoveRowClick)
                }) {
                    Text(text = "Remove Row")
                }
            }

            LazyColumn {
                itemsIndexed(state.mouseList) {index, mouse ->
                    MouseMultiItem(
                        trapIdList = state.trapIdList,
                        specieList = state.specieList,
                        isTrapIdExpanded = mouse.isTrapIdExpanded,
                        isSpecieExpanded = mouse.isSpecieExpanded,
                        onTrapIDClick = {
                            onEvent(
                                MouseMultiScreenEvent.OnTrapIdClick(index, mouse.trapID)
                            )
                        },
                        onSpecieClick = {
                            mouse.specie?.let { specie ->
                                onEvent(
                                    MouseMultiScreenEvent.OnSpecieClick(index, specie)
                                )
                            }
                        },
                        onTrapIdDismissClick = { onEvent(MouseMultiScreenEvent.OnTrapIdDismissClick(index)) },
                        onSpecieDismissClick = { onEvent(MouseMultiScreenEvent.OnSpecieDismissClick(index)) },
                    )
                }
            }
        }
    }
}