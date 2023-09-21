package com.example.datatrap.mouse.domain.use_case

import com.example.datatrap.camera.data.mouse_image.MouseImageRepository
import com.example.datatrap.mouse.data.MouseRepository

class DeleteMouseUseCase(
    private val mouseRepository: MouseRepository,
    private val mouseImageRepository: MouseImageRepository,
) {
    suspend operator fun invoke(mouseId: String) {
        val mouseImage = mouseImageRepository.getImageForMouse(mouseId)
        mouseImage?.let {
            mouseImageRepository.deleteImage(mouseImage)
        }
        val mouse = mouseRepository.getMouse(mouseId)
        mouseRepository.deleteMouse(mouse)
    }
}