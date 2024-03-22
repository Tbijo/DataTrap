package com.example.datatrap.occasion.domain.use_case

import com.example.datatrap.camera.data.occasion_image.OccasionImageEntity
import com.example.datatrap.camera.data.occasion_image.OccasionImageRepository
import com.example.datatrap.core.data.storage.InternalStorageRepository
import com.example.datatrap.occasion.data.occasion.OccasionEntity
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.session.data.SessionRepository
import java.time.ZonedDateTime

class InsertOccasionUseCase(
    private val occasionRepository: OccasionRepository,
    private val sessionRepository: SessionRepository,
    private val occasionImageRepository: OccasionImageRepository,
    private val internalStorageRepository: InternalStorageRepository,
) {
    // Insert Occasion, po pridani update Session numOcc + 1

    suspend operator fun invoke(sessionID: String, occasionEntity: OccasionEntity, imageName: String?, note: String?) {
        val session = sessionRepository.getSession(sessionID)

        sessionRepository.insertSession(
            session.copy(
                numOcc = session.numOcc + 1,
                sessionDateTimeUpdated = ZonedDateTime.now(),
            )
        )

        occasionRepository.insertOccasion(occasionEntity)

        insertOccasionImage(
            occasionId = occasionEntity.occasionId,
            imageName = imageName,
            note = note,
        )
    }

    private suspend fun insertOccasionImage(occasionId: String, imageName: String?, note: String?) {
        val currentOccasionImage = occasionImageRepository.getImageForOccasion(occasionId)

        // name not given nothing changed
        if (imageName == null && currentOccasionImage == null) return

        // name not given if image exists delete it
        if (imageName == null) {
            currentOccasionImage?.let {
                occasionImageRepository.deleteImage(it)
            }
            return
        }

        // if data is the same do not execute just return
        if (
            currentOccasionImage?.imgName == imageName &&
            currentOccasionImage.note == note
        ) {
            return
        }

        val occasionImage = if (currentOccasionImage == null) {
            // Create a new image
            OccasionImageEntity(
                imgName = imageName,
                occasionID = occasionId,
                path = internalStorageRepository.getImagePath(imageName) ?: "",
                note = note,
                dateTimeCreated = ZonedDateTime.now(),
                dateTimeUpdated = null,
            )
        } else {
            // Update an old image
            // If imageName changed so did the path
            val path = if (imageName == currentOccasionImage.imgName) {
                currentOccasionImage.path
            } else {
                // If imageName changed delete old image from storage
                internalStorageRepository.deleteImage(currentOccasionImage.imgName)

                internalStorageRepository.getImagePath(imageName) ?: ""
            }

            OccasionImageEntity(
                occasionImgId = currentOccasionImage.occasionImgId,
                imgName = imageName,
                occasionID = currentOccasionImage.occasionID,
                path = path,
                note = note,
                dateTimeCreated = currentOccasionImage.dateTimeCreated,
                dateTimeUpdated = ZonedDateTime.now(),
            )
        }

        occasionImageRepository.insertImage(occasionImage)
    }
}