package com.example.datatrap.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.datatrap.R

@Composable
fun ImageDrawer() {
    // binding.ivSelectImage.setImageURI(uriString.toUri())
    Image(painter = painterResource(id = R.drawable.empty), contentDescription = "selected image")
}