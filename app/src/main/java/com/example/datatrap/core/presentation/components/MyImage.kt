package com.example.datatrap.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.example.datatrap.R

@Composable
fun MyImage(
    modifier: Modifier = Modifier,
    imagePath: String?,
    contentDescription: String? = null,
    onClick: () -> Unit = {},
) {
    AsyncImage(
        modifier =  modifier.clickable { onClick() },
        model = imagePath ?: R.drawable.empty,
        contentDescription = contentDescription,
    )
}