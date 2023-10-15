package com.example.datatrap.mouse.domain.use_case

import com.example.datatrap.camera.data.mouse_image.MouseImageRepository
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.project.data.ProjectRepository
import com.example.datatrap.session.data.SessionRepository
import java.time.ZonedDateTime

class DeleteMouseUseCase(
    private val mouseRepository: MouseRepository,
    private val mouseImageRepository: MouseImageRepository,
    private val projectRepository: ProjectRepository,
    private val sessionRepository: SessionRepository,
    private val occasionRepository: OccasionRepository,
) {
    suspend operator fun invoke(mouseId: String) {
        val mouseImage = mouseImageRepository.getImageForMouse(mouseId)
        mouseImage?.let {
            mouseImageRepository.deleteImage(mouseImage)
        }

        val mouse = mouseRepository.getMouse(mouseId)

        mouse.primeMouseID?.let {
            // update Occasion
            val occasion = occasionRepository.getOccasion(mouse.occasionID)
            occasionRepository.insertOccasion(
                occasion.copy(
                    numMice = occasion.numMice?.minus(1),
                    occasionDateTimeUpdated = ZonedDateTime.now(),
                )
            )

            // update Project
            val session = sessionRepository.getSession(occasion.sessionID)
            val project = projectRepository.getProjectById(session.projectID)
            projectRepository.insertProject(
                project.copy(
                    numMice = project.numMice - 1,
                    projectDateTimeUpdated = ZonedDateTime.now(),
                )
            )
        }

        mouseRepository.deleteMouse(mouse)
    }
}