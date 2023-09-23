package com.example.datatrap.mouse.presentation.mouse_add_multi.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.specie.data.SpecieEntity

@Composable
fun MouseMultiItem(
    trapIdList: List<Int>,
    specieList: List<SpecieEntity>,
    isTrapIdExpanded: Boolean,
    isSpecieExpanded: Boolean,
    onTrapIDClick: (trapID: Int) -> Unit,
    onSpecieClick: (specie: SpecieEntity) -> Unit,
    onTrapIdDismissClick: () -> Unit,
    onSpecieDismissClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        DropdownMenu(expanded = isTrapIdExpanded, onDismissRequest = { onTrapIdDismissClick() }) {
            DropdownMenuItem(onClick = {}, enabled = false) {
                Text(text = "TrapID*")
            }
            trapIdList.forEach {
                DropdownMenuItem(onClick = { onTrapIDClick(it) }) {
                    Text(text = "$it")
                }
            }
        }

        DropdownMenu(expanded = isSpecieExpanded, onDismissRequest = { onSpecieDismissClick() }) {
            DropdownMenuItem(onClick = {}, enabled = false) {
                Text(text = "Specie*")
            }
            specieList.forEach {
                DropdownMenuItem(onClick = { onSpecieClick(it) }) {
                    Text(text = it.speciesCode)
                }
            }
        }
    }
}