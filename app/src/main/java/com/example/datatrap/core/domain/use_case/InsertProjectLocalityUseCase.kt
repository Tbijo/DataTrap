package com.example.datatrap.core.domain.use_case

import com.example.datatrap.core.data.project_locality.ProjectLocalityCrossRef
import com.example.datatrap.core.data.project_locality.ProjectLocalityRepository
import com.example.datatrap.project.data.ProjectRepository
import java.time.ZonedDateTime

/*
* Create DAO and Entity for Project and Locality
* Every time new Combination is made num of locals in a project goes up and vice versa
* A Combination is created only if it does not exist
* The numLocal in a Project goes up only when the combination does not exist*/

// Insert PrjLocCrossRef, po pridani kombinacie sa updatne Project numLocal + 1

class InsertProjectLocalityUseCase(
    private val projectLocalityRepository: ProjectLocalityRepository,
    private val projectRepository: ProjectRepository,
) {
    suspend operator fun invoke(localityId: String, projectId: String) {
        val existsComb = projectLocalityRepository.existsProjectLocalityCrossRef(localityId, projectId)

        if (existsComb) return

        // create combination
        val projectLocalityCrossRef = ProjectLocalityCrossRef(projectId, localityId)
        projectLocalityRepository.insertProjectLocality(projectLocalityCrossRef)

        // update project
        val project = projectRepository.getProjectById(projectId)
        projectRepository.insertProject(
            project.copy(
                numLocal = project.numLocal + 1,
                projectDateTimeUpdated = ZonedDateTime.now(),
            )
        )
    }
}