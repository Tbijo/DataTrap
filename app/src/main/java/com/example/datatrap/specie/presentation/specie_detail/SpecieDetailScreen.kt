package com.example.datatrap.specie.presentation.specie_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.datatrap.R
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.LabeledText
import com.example.datatrap.core.presentation.components.MyScaffold

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
    MyScaffold(
        title = "Specie Detail",
        errorState = state.error,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState()),
        ) {
            Image(painter = painterResource(id = R.drawable.empty), contentDescription = "Specie Image")

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                LabeledText(label = "Specie Code", text = "AAG")
                LabeledText(label = "Full Name", text = "Agrelus Gussu")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                LabeledText(label = "Authority", text = "Author")
                LabeledText(label = "Synonymum", text = "Mouse Name")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LabeledText(label = "Description", text = "Description... more and more")

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                LabeledText(label = "Min. Weight (g)", text = "5")
                LabeledText(label = "Max. Weight (g)", text = "60")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                LabeledText(label = "Body Length (mm)", text = "1")
                LabeledText(label = "Tail Length (mm)", text = "12")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                LabeledText(label = "Min. Feet Length (mm)", text = "10")
                LabeledText(label = "Max. Feet Length (mm)", text = "11")
            }

            LabeledText(label = "Number of fingers on upper limb", text = "4")

            LabeledText(label = "Note", text = "Note... note note note note.")
        }
    }
}