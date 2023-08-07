package com.example.datatrap.specie.presentation.specie_list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.specie.presentation.specie_list.components.SpecieListItem

@Composable
fun SpecieListScreen() {
    val species = listOf("AAG")
    Scaffold {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            items(species) {
                SpecieListItem(specieCode = it, specieLatin = "Agrelus Akulus")
            }
        }
    }
}