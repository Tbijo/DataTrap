package com.example.datatrap.mouse.presentation.mouse_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.LabeledText
import com.example.datatrap.core.presentation.components.MyBottomSheetScaffold
import com.example.datatrap.core.presentation.components.MyImage

@Composable
fun MouseDetailScreen(
    onEvent: (MouseDetailScreenEvent) -> Unit,
    state: MouseDetailUiState,
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
    onEvent: (MouseDetailScreenEvent) -> Unit,
    state: MouseDetailUiState,
) {
    MyBottomSheetScaffold(
        title = "Individual Code: ${state.mouseView?.code ?: "NONE"}",
        errorState = state.error,
        sheetContent = {
            MyImage(
                modifier = Modifier.fillMaxWidth(),
                imagePath = state.mouseImagePath,
                contentDescription = "Mouse image",
                onClick = {
                    onEvent(MouseDetailScreenEvent.OnImageClick)
                },
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it),
        ) {
            MyImage(
                imagePath = state.mouseImagePath,
                contentDescription = "Mouse image",
                onClick = {
                    onEvent(MouseDetailScreenEvent.OnImageClick)
                },
            )

            Row {
                LabeledText(label = "Project", text = state.mouseView?.projectName ?: "None")
                LabeledText(label = "Specie Code", text = state.mouseView?.specieCode ?: "None")
                LabeledText(label = "Species Full Name", text = state.mouseView?.specieFullName ?: "None")
            }

            Row {
                LabeledText(label = "Weight (g)", text = "${state.mouseView?.weight ?: "None"}")
                LabeledText(label = "Age", text = state.mouseView?.age ?: "None")
                LabeledText(label = "Sex", text = state.mouseView?.sex ?: "None")
            }

            Row {
                LabeledText(label = "Gravidity", text = state.mouseView?.gravidity ?: "None")
                LabeledText(label = "Lactating", text = state.mouseView?.lactating?: "None")
                LabeledText(label = "Sex. Active", text = state.mouseView?.sexActive ?: "None")
            }

            Row {
                LabeledText(label = "Body Length (mm)", text = "${state.mouseView?.body ?: "None"}")
                LabeledText(label = "Tail Length (mm)", text = "${state.mouseView?.tail ?: "None"}")
                LabeledText(label = "Feet Length (mm)", text = "${state.mouseView?.feet ?: "None"}")
                LabeledText(label = "Ear Length (mm)", text = "${state.mouseView?.ear ?: "None"}")
            }

            Row {
                LabeledText(label = "Catch DateTime", text = state.mouseView?.mouseCaughtDateTime ?: "None")
                LabeledText(label = "Legitimation", text = state.mouseView?.legit ?: "None")
                LabeledText(label = "Note", text = state.mouseView?.note ?: "None")
            }

            Row {
                LabeledText(label = "Testes Length", text = "${state.mouseView?.testesLength ?: "None"}")
                LabeledText(label = "Testes Width", text = "${state.mouseView?.testesWidth ?: "None"}")
            }

            Row {
                LabeledText(label = "Embryo Right", text = "${state.mouseView?.embryoRight ?: "None"}")
                LabeledText(label = "Embryo Left", text = "${state.mouseView?.embryoLeft ?: "None"}")
                LabeledText(label = "Embryo Diameter", text = "${state.mouseView?.embryoDiameter ?: "None"}")
            }

            Row {
                LabeledText(label = "MC Right", text = "${state.mouseView?.mcRight ?: "None"}")
                LabeledText(label = "MC Left", text = "${state.mouseView?.mcLeft ?: "None"}")
                LabeledText(label = "MC", text = state.mouseView?.mc ?: "None")
            }

            // History of previous captures
            LazyColumn {
                items(state.logList) {log ->
                    Text(text = log)
                }
            }
        }
    }
}