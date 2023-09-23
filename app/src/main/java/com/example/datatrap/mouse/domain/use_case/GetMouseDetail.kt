package com.example.datatrap.mouse.domain.use_case

import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.mouse.domain.model.MouseView
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.project.data.ProjectRepository
import com.example.datatrap.session.data.SessionRepository
import com.example.datatrap.specie.data.SpecieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.format.DateTimeFormatter

class GetMouseDetail(
    private val mouseRepository: MouseRepository,
    private val specieRepository: SpecieRepository,
    private val occasionRepository: OccasionRepository,
    private val sessionRepository: SessionRepository,
    private val projectRepository: ProjectRepository,
) {
    // data for MouseDetailScreen
    operator fun invoke(mouseId: String): Flow<MouseView> = flow {
        val mouse = mouseRepository.getMouse(mouseId)
        val specie = specieRepository.getSpecie(mouse.speciesID)
        val occasion = occasionRepository.getOccasion(mouse.occasionID)
        val session = sessionRepository.getSession(occasion.sessionID)
        val project = projectRepository.getProjectById(session.projectID)

        val mouseView = MouseView(
            mouseId = mouse.mouseId,
            primeMouseId = mouse.primeMouseID,
            code = mouse.code,
            body = mouse.body,
            tail = mouse.tail,
            feet = mouse.feet,
            ear = mouse.ear,
            mouseCaughtDateTime = mouse.mouseCaught.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            gravidity = if (mouse.gravidity == true) "Yes" else "No",
            lactating = if (mouse.lactating == true) "Yes" else "No",
            sexActive = if (mouse.sexActive == true) "Yes" else "No",
            age = mouse.age,
            sex = mouse.sex,
            weight = mouse.weight,
            note = mouse.note,
            testesLength = mouse.testesLength,
            testesWidth = mouse.testesWidth,
            embryoRight = mouse.embryoRight,
            embryoLeft = mouse.embryoLeft,
            embryoDiameter = mouse.embryoDiameter,
            mc = when(mouse.MC) {
                true -> "Yes"
                false -> "No"
                else -> "null"
            },
            mcRight = mouse.MCright,
            mcLeft = mouse.MCleft,
            specieFullName = specie.fullName,
            specieCode = specie.speciesCode,
            legit = occasion.leg,
            projectName = project.projectName,
        )

        emit(mouseView)
    }
}