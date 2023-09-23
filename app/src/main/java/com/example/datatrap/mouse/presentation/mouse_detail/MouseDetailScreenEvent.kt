package com.example.datatrap.mouse.presentation.mouse_detail

sealed interface MouseDetailScreenEvent {
    object OnImageClick: MouseDetailScreenEvent
}