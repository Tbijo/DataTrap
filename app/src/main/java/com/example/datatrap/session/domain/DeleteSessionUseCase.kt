package com.example.datatrap.session.domain

import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.project.data.ProjectRepository
import java.time.ZonedDateTime

class DeleteSessionUseCase(
    private val projectRepository: ProjectRepository,
    private val occasionRepository: OccasionRepository,
) {
    // DELETE Session, po vymazani update Project znizit numMice o vsetky pocty mysi v jej Occasionov

    suspend operator fun invoke(sessionId: String, projectID: String) {
        val project = projectRepository.getProjectById(projectID)
        var numMiceOfOccasions = 0

        occasionRepository.getOccasionsForSession(sessionId).collect { occasions ->
            numMiceOfOccasions = occasions.sumOf {
                it.numMice ?: 0
            }
        }

        projectRepository.insertProject(
            project.copy(
                numMice = project.numMice - numMiceOfOccasions,
                projectDateTimeUpdated = ZonedDateTime.now(),
            )
        )
    }
}