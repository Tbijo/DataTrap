package com.example.datatrap.camera.presentation

import android.graphics.Bitmap

sealed interface CameraScreenEvent {
    data class OnImageReceived(val bitmap: Bitmap): CameraScreenEvent
    data class OnNoteTextChanged(val text: String): CameraScreenEvent
    object OnDeleteImage: CameraScreenEvent
    data class OnLeave(val makeChange: Boolean): CameraScreenEvent
}