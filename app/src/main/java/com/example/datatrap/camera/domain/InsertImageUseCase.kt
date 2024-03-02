package com.example.datatrap.camera.domain

import com.example.datatrap.camera.data.mouse_image.MouseImageEntity
import com.example.datatrap.camera.data.mouse_image.MouseImageRepository
import com.example.datatrap.camera.data.occasion_image.OccasionImageEntity
import com.example.datatrap.camera.data.occasion_image.OccasionImageRepository
import com.example.datatrap.camera.util.EntityType
import com.example.datatrap.core.data.storage.InternalStorageRepository
import java.time.ZonedDateTime

class InsertImageUseCase(
    private val mouseImageRepository: MouseImageRepository,
    private val occasionImageRepository: OccasionImageRepository,
    private val internalStorageRepository: InternalStorageRepository,
) {
    // TODO Create and show image in CameraScreen
    // TODO Return imagePath (+ image note) to previous screen
    // TODO Add Inserting image logic to Inserting Occasion/Mouse logic IF imagePath exists
    // TODO After all inserting is done navigate back from Occasion/Mouse screen

    suspend operator fun invoke(entity: EntityType, entityId: String, imageName: String, note: String?) {
        when(entity) {
            EntityType.MOUSE -> {
                insertMouseImage(entityId, imageName, note)
            }

            EntityType.OCCASION -> {
                insertOccasionImage(entityId, imageName, note)
            }
        }
    }

    private suspend fun insertOccasionImage(occasionId: String, imageName: String, note: String?) {
        // TODO If imageName changed so did the path
        val currentOccasionImage = occasionImageRepository.getImageForOccasion(occasionId)

        val occasionImage = if (currentOccasionImage == null) {
            OccasionImageEntity(
                imgName = imageName,
                occasionID = occasionId,
                path = internalStorageRepository.getImagePath(imageName) ?: "",
                note = note,
                dateTimeCreated = ZonedDateTime.now(),
                dateTimeUpdated = null,
            )
        }
        else {
            // delete previous one if there was one
            if (imageName != currentOccasionImage.imgName) {
                internalStorageRepository.deleteImage(currentOccasionImage.imgName)
            }
        // todo check which values can be updated
            OccasionImageEntity(
                occasionImgId = currentOccasionImage.occasionImgId,
                imgName = imageName,
                occasionID = currentOccasionImage.occasionID,
                path = currentOccasionImage.path,
                note = note,
                dateTimeCreated = currentOccasionImage.dateTimeCreated,
                dateTimeUpdated = ZonedDateTime.now(),
            )
        }

        occasionImageRepository.insertImage(occasionImage)
    }

    private suspend fun insertMouseImage(mouseId: String, imageName: String, note: String?) {
        // TODO If imageName changed so did the path
        val currentMouseImage = mouseImageRepository.getImageForMouse(mouseId)

        val mouseImageEntity = if (currentMouseImage == null) {
            MouseImageEntity(
                imgName = imageName,
                mouseID = mouseId,
                path = internalStorageRepository.getImagePath(imageName) ?: "",
                note = note,
                dateTimeCreated = ZonedDateTime.now(),
                dateTimeUpdated = null,
            )
        } else {
            // delete previous one if there was one
            if (imageName != currentMouseImage.imgName) {
                internalStorageRepository.deleteImage(currentMouseImage.imgName)
            }
            // todo check which values can be updated

            MouseImageEntity(
                mouseImgId = currentMouseImage.mouseImgId,
                imgName = imageName,
                mouseID = currentMouseImage.mouseID,
                path = currentMouseImage.path,
                note = note,
                dateTimeCreated = currentMouseImage.dateTimeCreated,
                dateTimeUpdated = ZonedDateTime.now(),
            )
        }

        mouseImageRepository.insertImage(mouseImageEntity)
    }
}