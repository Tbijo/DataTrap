package com.example.datatrap.core.domain

import com.example.datatrap.core.domain.model.InfoNames
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.project.data.ProjectRepository
import com.example.datatrap.session.data.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetInfoNamesUseCase(
    private val projectRepository: ProjectRepository,
    private val localityRepository: LocalityRepository? = null,
    private val sessionRepository: SessionRepository? = null,
    private val occasionRepository: OccasionRepository? = null,
) {
    operator fun invoke(
        projectId: String,
        localityId: String?,
        sessionId: String?,
        occasionId: String?,
    ): Flow<InfoNames> = flow {
        val projectName: String
        var localityName: String? = null
        var sessionNum: Int? = null
        var occasionNum: Int? = null

        val project = projectRepository.getProjectById(projectId)
        projectName = project.projectName

        localityRepository?.let {
            localityId?.let {
                val locality = localityRepository.getLocality(localityId)
                localityName = locality.localityName
            }
        }

        sessionRepository?.let {
            sessionId?.let {
                val session = sessionRepository.getSession(sessionId)
                sessionNum = session.session
            }
        }

        occasionRepository?.let {
            occasionId?.let {
                val occasion = occasionRepository.getOccasion(occasionId)
                occasionNum = occasion.occasion
            }
        }

        val data = InfoNames(
            projectName = projectName,
            localityName = localityName,
            sessionNum = sessionNum,
            occasionNum = occasionNum,
        )

        emit(data)
    }
}