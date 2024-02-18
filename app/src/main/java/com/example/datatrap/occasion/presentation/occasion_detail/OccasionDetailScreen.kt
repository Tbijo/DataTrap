package com.example.datatrap.occasion.presentation.occasion_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.LabeledText
import com.example.datatrap.core.presentation.components.MyBottomSheetScaffold
import com.example.datatrap.core.presentation.components.MyImage
import java.time.format.DateTimeFormatter

@Composable
fun OccasionDetailScreen(
    onEvent: (OccasionDetailScreenEvent) -> Unit,
    state: OccasionDetailUiState,
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
    onEvent: (OccasionDetailScreenEvent) -> Unit,
    state: OccasionDetailUiState,
) {
    MyBottomSheetScaffold(
        title = "Occasion Detail",
        errorState = state.error,
        sheetContent = {
            MyImage(
                modifier = Modifier.fillMaxWidth(),
                imagePath = state.imagePath,
                contentDescription = "Occasion Image",
                onClick = {
                    onEvent(OccasionDetailScreenEvent.OnImageClick)
                },
            )
        },
    ) {
        Column(
            modifier = Modifier.padding(it).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp, alignment = Alignment.Top),
        ) {

            MyImage(
                imagePath = state.imagePath,
                contentDescription = "Occasion Image",
                onClick = {
                    onEvent(OccasionDetailScreenEvent.OnImageClick)
                },
            )

            Row {
                LabeledText(label = "Order of Occasion in Session", text = "${state.occasionEntity?.occasion}")
                LabeledText(label = "Locality", text = state.localityName)
            }

            Row {
                LabeledText(label = "Method", text = state.methodName)
                LabeledText(label = "MethodType", text = state.methodTypeName)
            }

            Row {
                LabeledText(label = "TrapType", text = state.trapTypeName)
                LabeledText(label = "Environment Type", text = state.envTypeName)
            }

            Row {
                LabeledText(label = "VegType", text = state.vegTypeName)
                LabeledText(label = "Occasion Created DateTime",
                    text = "${state.occasionEntity?.occasionDateTimeCreated?.format(
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME)}"
                )
            }

            Row {
                LabeledText(label = "Number of Traps", text = "${state.occasionEntity?.numTraps}")
                LabeledText(label = "Number of Mice Caught", text = "${state.occasionEntity?.numMice}")
            }

            Row {
                LabeledText(label = "Temperature °C", text = "${state.occasionEntity?.temperature}")
                LabeledText(label = "Weather", text = "${state.occasionEntity?.weather}")
            }

            Row {
                LabeledText(label = "Legitimation", text = "${state.occasionEntity?.leg}")
                LabeledText(label = "Caught something", text = if (state.occasionEntity?.gotCaught == true) "Yes" else "No")
            }

            Row {
                LabeledText(label = "Number of Species", text = "${state.specieNum}")
                LabeledText(label = "Number of Errors", text = "${state.errorNum}")
                LabeledText(label = "Number of Close", text = "${state.closeNum}")
            }

            Row {
                LabeledText(label = "Number of Predator", text = "${state.predatorNum}")
                LabeledText(label = "Number of PVP", text = "${state.pvpNum}")
                LabeledText(label = "Number of Other", text = "${state.otherNum}")
            }

            LabeledText(label = "Note", text = "${state.occasionEntity?.note}")
        }
    }
}