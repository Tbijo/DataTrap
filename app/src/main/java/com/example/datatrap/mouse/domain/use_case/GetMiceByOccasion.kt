package com.example.datatrap.mouse.domain.use_case

import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.mouse.domain.model.MouseListData
import com.example.datatrap.specie.data.SpecieEntity
import com.example.datatrap.specie.data.SpecieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.format.DateTimeFormatter

class GetMiceByOccasion(
    private val mouseRepository: MouseRepository,
    private val specieRepository: SpecieRepository,
) {
    suspend operator fun invoke(occasionID: String): Flow<List<MouseListData>> {
        var species = emptyList<SpecieEntity>()

        specieRepository.getSpecies().collect {
            species = it
        }

        return mouseRepository.getMiceForOccasion(occasionID).map { mice ->
            mice.map {
                MouseListData(
                    mouseId = it.mouseId,
                    primeMouseID = it.primeMouseID,
                    mouseCode = it.code,
                    specieCode = species.find { specie -> specie.specieId == it.speciesID }?.speciesCode ?: "NONE",
                    mouseCaught = it.mouseCaught.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    sex = it.sex,
                )
            }
        }
    }
}