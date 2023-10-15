package com.example.datatrap.mouse.domain.use_case

import com.example.datatrap.core.util.EnumSpecie
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
) {

// Mouse Insert, po pridani novej mysi ak neni specieID = TRE, Close, P, PVP, Other tak updatne sa gotCaught v Occasion

// Mouse Recap Insert, po pridani ak je sex prveho zaznamu iny ako aktualny tak sa vsetky nastavia podla aktualneho

    suspend operator fun invoke(occasionID: String) {
        val mouse = mouseRepository.getMiceForOccasion(occasionID).maxBy { it.mouseDateTimeCreated }

        // update occasion numMice
        val occasion = occasionRepository.getOccasion(mouse.occasionID)
        val nonSpecies = EnumSpecie.values().map { it.name }

        val filteredSpecies = specieRepository.getSpecies()
            .filter { it.speciesCode in nonSpecies }
            .map { it.specieId }

        occasionRepository.insertOccasion(
            occasion.copy(
                numMice = occasion.numMice?.plus(1),
                gotCaught = mouse.speciesID !in filteredSpecies,
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
        if (mouse.primeMouseID != null && mouse.sex != null) {
            val previousMice = mouseRepository.getMiceByPrimeMouseID(mouse.primeMouseID)
                .filter { it.sex != mouse.sex }
                .map { it.copy(
                    sex = mouse.sex,
                    mouseDateTimeUpdated = ZonedDateTime.now(),
                ) }.toMutableList()

            val firstMouse = mouseRepository.getMouse(mouse.primeMouseID)
            if (firstMouse.sex != mouse.sex) {
                previousMice.add(
                    firstMouse.copy(
                        sex = mouse.sex,
                        mouseDateTimeUpdated = ZonedDateTime.now(),
                    )
                )
            }

            if (previousMice.isNotEmpty()) {
                mouseRepository.insertMice(previousMice)
            }
        }
    }
}