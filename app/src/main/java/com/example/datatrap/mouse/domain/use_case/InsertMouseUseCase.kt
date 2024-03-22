package com.example.datatrap.mouse.domain.use_case

import com.example.datatrap.camera.data.mouse_image.MouseImageEntity
import com.example.datatrap.camera.data.mouse_image.MouseImageRepository
import com.example.datatrap.core.data.storage.InternalStorageRepository
import com.example.datatrap.core.util.EnumSpecie
import com.example.datatrap.mouse.data.MouseEntity
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.project.data.ProjectRepository
import com.example.datatrap.session.data.SessionRepository
import com.example.datatrap.specie.data.SpecieRepository
import java.time.ZonedDateTime

class InsertMouseUseCase(
    private val mouseRepository: MouseRepository,
    private val projectRepository: ProjectRepository,
    private val sessionRepository: SessionRepository,
    private val occasionRepository: OccasionRepository,
    private val specieRepository: SpecieRepository,
    private val mouseImageRepository: MouseImageRepository,
    private val internalStorageRepository: InternalStorageRepository,
) {
    // Mouse Insert, po pridani novej mysi ak neni specieID = TRE, Close, P, PVP, Other tak updatne sa gotCaught v Occasion
    // Mouse Recap Insert, po pridani ak je sex prveho zaznamu iny ako aktualny tak sa vsetky nastavia podla aktualneho

    suspend operator fun invoke(occasionID: String, mouseEntity: MouseEntity, imageName: String?, imageNote: String?) {
        var mouse: MouseEntity? = null

        mouseRepository.getMiceForOccasion(occasionID).collect { mouseList ->
            mouse = mouseList.maxBy { it.mouseDateTimeCreated }
        }

        // update occasion numMice
        mouse?.let { notNullMouse ->
            val occasion = occasionRepository.getOccasion(notNullMouse.occasionID)
            val nonSpecies = EnumSpecie.values().map { it.name }
            var filteredSpecies = emptyList<String>()

            specieRepository.getSpecies().collect { specieList ->
                filteredSpecies = specieList
                    .filter { it.speciesCode in nonSpecies }
                    .map { it.specieId }
            }

            occasionRepository.insertOccasion(
                occasion.copy(
                    numMice = occasion.numMice?.plus(1),
                    gotCaught = notNullMouse.speciesID !in filteredSpecies,
                    occasionDateTimeUpdated = ZonedDateTime.now(),
                )
            )

            // update project
            val session = sessionRepository.getSession(occasion.sessionID)
            val project = projectRepository.getProjectById(session.projectID)

            projectRepository.insertProject(
                project.copy(
                    numMice = project.numMice + 1,
                    projectDateTimeUpdated = ZonedDateTime.now(),
                )
            )

            // Update sexes
            if (notNullMouse.primeMouseID != null && notNullMouse.sex != null) {
                val previousMice = mouseRepository.getMiceByPrimeMouseID(notNullMouse.primeMouseID)
                    .filter { it.sex != notNullMouse.sex }
                    .map { it.copy(
                        sex = notNullMouse.sex,
                        mouseDateTimeUpdated = ZonedDateTime.now(),
                    ) }.toMutableList()

                val firstMouse = mouseRepository.getMouse(notNullMouse.primeMouseID)

                if (firstMouse.sex != notNullMouse.sex) {
                    previousMice.add(
                        firstMouse.copy(
                            sex = notNullMouse.sex,
                            mouseDateTimeUpdated = ZonedDateTime.now(),
                        )
                    )
                }

                if (previousMice.isNotEmpty()) {
                    mouseRepository.insertMice(previousMice)
                }
            }
        }

        mouseRepository.insertMouse(mouseEntity)

        insertMouseImage(
            mouseId = mouseEntity.mouseId,
            imageName = imageName,
            note = imageNote,
        )
    }

    private suspend fun insertMouseImage(mouseId: String, imageName: String?, note: String?) {
        val currentMouseImage = mouseImageRepository.getImageForMouse(mouseId)

        // name not given nothing changed
        if (imageName == null && currentMouseImage == null) return

        // name not given if image exists delete it
        if (imageName == null) {
            currentMouseImage?.let {
                mouseImageRepository.deleteImage(it)
            }
            return
        }

        // if data is the same do not execute just return
        if (
            currentMouseImage?.imgName == imageName &&
            currentMouseImage.note == note
        ) {
            return
        }

        val mouseImageEntity = if (currentMouseImage == null) {
            // Create a new image
            MouseImageEntity(
                imgName = imageName,
                mouseID = mouseId,
                path = internalStorageRepository.getImagePath(imageName) ?: "",
                note = note,
                dateTimeCreated = ZonedDateTime.now(),
                dateTimeUpdated = null,
            )
        } else {
            // Update an old image
            // If imageName changed so did the path
            val path = if (imageName == currentMouseImage.imgName) {
                currentMouseImage.path
            } else {
                // If imageName changed delete old image from storage
                internalStorageRepository.deleteImage(currentMouseImage.imgName)

                internalStorageRepository.getImagePath(imageName) ?: ""
            }

            MouseImageEntity(
                mouseImgId = currentMouseImage.mouseImgId,
                imgName = imageName,
                mouseID = currentMouseImage.mouseID,
                path = path,
                note = note,
                dateTimeCreated = currentMouseImage.dateTimeCreated,
                dateTimeUpdated = ZonedDateTime.now(),
            )
        }

        mouseImageRepository.insertImage(mouseImageEntity)
    }

}