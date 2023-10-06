package com.example.datatrap.specie.presentation.specie_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.LabeledText
import com.example.datatrap.core.presentation.components.MyBottomSheetScaffold
import com.example.datatrap.core.presentation.components.MyImage

@Composable
fun SpecieDetailScreen(
    onEvent: (SpecieDetailScreenEvent) -> Unit,
    state: SpecieDetailUiState,
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
    onEvent: (SpecieDetailScreenEvent) -> Unit,
    state: SpecieDetailUiState,
) {
    val scrollState = rememberScrollState()

    MyBottomSheetScaffold(
        title = "Specie Detail",
        errorState = state.error,
        isSheetExpanded = state.isSheetExpanded,
        sheetContent = {
            MyImage(
                modifier = Modifier.fillMaxWidth(),
                imagePath = state.imagePath,
                contentDescription = "Specie Image",
            ) {
                onEvent(SpecieDetailScreenEvent.OnImageClick)
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(scrollState),
        ) {
            MyImage(imagePath = state.imagePath, contentDescription = "Specie Image") {
                onEvent(SpecieDetailScreenEvent.OnImageClick)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                LabeledText(label = "Specie Code", text = "${state.specieEntity?.speciesCode}")
                LabeledText(label = "Full Name", text = "${state.specieEntity?.fullName}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                LabeledText(label = "Authority", text = "${state.specieEntity?.authority}")
                LabeledText(label = "Synonymum", text = "${state.specieEntity?.synonym}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LabeledText(label = "Description", text = "${state.specieEntity?.description}")

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                LabeledText(label = "Min. Weight (g)", text = "${state.specieEntity?.minWeight}")
                LabeledText(label = "Max. Weight (g)", text = "${state.specieEntity?.maxWeight}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                LabeledText(label = "Body Length (mm)", text = "${state.specieEntity?.bodyLength}")
                LabeledText(label = "Tail Length (mm)", text = "${state.specieEntity?.tailLength}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                LabeledText(label = "Min. Feet Length (mm)", text = "${state.specieEntity?.feetLengthMin}")
                LabeledText(label = "Max. Feet Length (mm)", text = "${state.specieEntity?.feetLengthMax}")
            }

            LabeledText(label = "Number of fingers on upper limb", text = "${state.specieEntity?.upperFingers}")

            LabeledText(label = "Note", text = "${state.specieEntity?.note}")
        }
    }
}