package com.example.datatrap.occasion.domain.use_case

import com.example.datatrap.camera.data.occasion_image.OccasionImageRepository
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.project.data.ProjectRepository
import com.example.datatrap.session.data.SessionRepository
import java.time.ZonedDateTime

class DeleteOccasionUseCase(
    private val occasionRepository: OccasionRepository,
    private val occasionImageRepository: OccasionImageRepository,
    private val sessionRepository: SessionRepository,
    private val projectRepository: ProjectRepository,
) {
    // Delete Occasion, po vymazani update Session numOcc - 1
    // Delete Occasion, po vymazani update Project zniz o pocet mysi v Occasion

    suspend operator fun invoke(occasionId: String, sessionID: String) {
        val occasionImage = occasionImageRepository.getImageForOccasion(occasionId)
        occasionImage?.let {
            occasionImageRepository.deleteImage(occasionImage)
        }

        // update session
        val session = sessionRepository.getSession(sessionID)
        sessionRepository.insertSession(
            session.copy(
                numOcc = session.numOcc - 1,
                sessionDateTimeUpdated = ZonedDateTime.now(),
            )
        )

        // update Project
        val occasion = occasionRepository.getOccasion(occasionId)
        val project = projectRepository.getProjectById(session.projectID)
        projectRepository.insertProject(
            project.copy(
                numMice = project.numMice - (occasion.numMice ?: 0),
                projectDateTimeUpdated = ZonedDateTime.now(),
            )
        )

        // Delete Occasion
        occasionRepository.deleteOccasion(occasion)
    }
}