package com.example.datatrap.camera.presentation

import android.graphics.Bitmap
import com.example.datatrap.camera.data.mouse_image.MouseImageEntity
import com.example.datatrap.camera.data.occasion_image.OccasionImageEntity

data class CameraUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val mouseImageEntity: MouseImageEntity? = null,
    val occasionImageEntity: OccasionImageEntity? = null,

    val imageName: String? = null,
    val bitmap: Bitmap? = null,
    val oldImageName: String? = null,

    val note: String? = null,
    val imageStateText: String = "",
)
