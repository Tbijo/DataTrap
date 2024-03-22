package com.example.datatrap.specie.domain.use_case

import android.net.Uri
import com.example.datatrap.specie.data.SpecieEntity
import com.example.datatrap.specie.data.SpecieRepository
import com.example.datatrap.specie.data.specie_image.SpecieImageEntity
import com.example.datatrap.specie.data.specie_image.SpecieImageRepository
import java.time.ZonedDateTime

class InsertSpecieUseCase(
    private val specieRepository: SpecieRepository,
    private val specieImageRepository: SpecieImageRepository,
) {
    suspend operator fun invoke(specieEntity: SpecieEntity, imageUri: String?, imageNote: String?) {
        specieRepository.insertSpecie(specieEntity)

        insertSpecieImage(
            specieId = specieEntity.specieId,
            imageUri = imageUri,
            imageNote = imageNote,
        )
    }

    private suspend fun insertSpecieImage(specieId: String, imageUri: String?, imageNote: String?) {
        val currentSpecieImage = specieImageRepository.getImageForSpecie(specieId)

        // uri not given nothing changed
        if (imageUri == null && currentSpecieImage == null) return

        // uri not given if image exists delete it
        if (imageUri == null) {
            currentSpecieImage?.let {
                specieImageRepository.deleteImage(it)
            }
            return
        }

        // if data is the same do not execute just return
        if (
            currentSpecieImage?.imageUri.toString() == imageUri &&
            currentSpecieImage?.note == imageNote
        ) {
            return
        }

        val specieImageEntity = if (currentSpecieImage == null) {
            // Create a new image
            SpecieImageEntity(
                imageUri = Uri.parse(imageUri),
                note = imageNote,
                specieID = specieId,
                dateTimeCreated = ZonedDateTime.now(),
                dateTimeUpdated = null,
            )
        } else {
            // Update an old image
            val uri = if (imageUri == currentSpecieImage.imageUri.toString()) {
                currentSpecieImage.imageUri.toString()
            } else {
                imageUri
            }

            SpecieImageEntity(
                specieImgId = currentSpecieImage.specieImgId,
                imageUri = Uri.parse(uri),
                note = imageNote,
                specieID = currentSpecieImage.specieID,
                dateTimeCreated = currentSpecieImage.dateTimeCreated,
                dateTimeUpdated = ZonedDateTime.now(),
            )
        }

        specieImageRepository.insertImage(specieImageEntity)
    }
}