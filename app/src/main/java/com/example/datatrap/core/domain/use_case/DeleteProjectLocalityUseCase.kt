package com.example.datatrap.core.domain.use_case

import com.example.datatrap.core.data.project_locality.ProjectLocalityCrossRef
import com.example.datatrap.core.data.project_locality.ProjectLocalityRepository
import com.example.datatrap.project.data.ProjectRepository
import java.time.ZonedDateTime

// Delete PrjLocCrossRef, po vymazani kombinacie sa updatne Project numLocal - 1

class DeleteProjectLocalityUseCase(
    private val projectLocalityRepository: ProjectLocalityRepository,
    private val projectRepository: ProjectRepository,
) {
    suspend operator fun invoke(localityId: String, projectId: String) {
        val projectLocalityCrossRef = ProjectLocalityCrossRef(projectId, localityId)
        projectLocalityRepository.deleteProjectLocality(projectLocalityCrossRef)

        // update Project
        val project = projectRepository.getProjectById(projectId)
        projectRepository.insertProject(
            project.copy(
                numLocal = project.numLocal - 1,
                projectDateTimeUpdated = ZonedDateTime.now(),
            )
        )
    }
}