package com.example.datatrap.mouse.presentation.mouse_add_multi.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.specie.domain.model.SpecList

@Composable
fun MouseMultiItem(
    trapIdList: List<Int>,
    specieList: List<SpecList>,
    isTrapIdExpanded: Boolean,
    isSpecieExpanded: Boolean,
    onTrapIDClick: () -> Unit,
    onSpecieClick: () -> Unit,
    onTrapIdDismissClick: () -> Unit,
    onSpecieDismissClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        DropdownMenu(expanded = isTrapIdExpanded, onDismissRequest = { onTrapIdDismissClick() }) {
            DropdownMenuItem(
                onClick = {},
                enabled = false,
                text = { Text(text = "TrapID*") },
            )
            trapIdList.forEach {
                DropdownMenuItem(
                    onClick = onTrapIDClick,
                    text = { Text(text = "$it") },
                )
            }
        }

        DropdownMenu(expanded = isSpecieExpanded, onDismissRequest = { onSpecieDismissClick() }) {
            DropdownMenuItem(
                onClick = {},
                enabled = false,
                text = { Text(text = "Specie*") },
            )
            specieList.forEach {
                DropdownMenuItem(
                    onClick = onSpecieClick,
                    text = { Text(text = it.speciesCode) },
                )
            }
        }
    }
}