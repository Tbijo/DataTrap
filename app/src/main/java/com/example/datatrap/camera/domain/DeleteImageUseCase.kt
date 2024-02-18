package com.example.datatrap.camera.domain

import com.example.datatrap.camera.data.mouse_image.MouseImageRepository
import com.example.datatrap.camera.data.occasion_image.OccasionImageRepository
import com.example.datatrap.core.data.storage.InternalStorageRepository
import com.example.datatrap.mouse.data.MouseEntity
import com.example.datatrap.occasion.data.occasion.OccasionEntity

class DeleteImageUseCase(
    private val internalStorageRepository: InternalStorageRepository,
    private val occasionImageRepository: OccasionImageRepository,
    private val mouseImageRepository: MouseImageRepository,
) {
    suspend operator fun invoke(
        occasionEntity: OccasionEntity? = null,
        mouseEntity: MouseEntity? = null,
    ) {
        mouseEntity?.let {
            val mouseImageEntity = mouseImageRepository.getImageForMouse(mouseEntity.mouseId)
            mouseImageEntity?.let {
                // Delete file image
                internalStorageRepository.deleteImage(mouseImageEntity.imgName)
                // Delete data image
                mouseImageRepository.deleteImage(mouseImageEntity)
            }
        }

        occasionEntity?.let {
            val occasionImageEntity = occasionImageRepository.getImageForOccasion(occasionEntity.occasionId)
            occasionImageEntity?.let {
                // Delete file image
                internalStorageRepository.deleteImage(occasionImageEntity.imgName)
                // Delete data image
                occasionImageRepository.deleteImage(occasionImageEntity)
            }
        }
    }

}