package com.example.datatrap.occasion.domain.use_case

import com.example.datatrap.core.util.EnumSpecie
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.occasion.domain.model.OccasionStats
import com.example.datatrap.specie.data.SpecieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CountSpecialCasesUseCase(
    private val mouseRepository: MouseRepository,
    private val specieRepository: SpecieRepository,
) {
    operator fun invoke(occasionId: String): Flow<OccasionStats> = flow {
        val nonSpecies = specieRepository.getNonSpecie()
        val nonSpeciesId = nonSpecies.map { it.specieId }

        val errorNonSpecieId = nonSpecies.find { it.speciesCode == EnumSpecie.TRE.name }?.specieId
        val closeNonSpecieId = nonSpecies.find { it.speciesCode == EnumSpecie.C.name }?.specieId
        val predatorNonSpecieId = nonSpecies.find { it.speciesCode == EnumSpecie.P.name }?.specieId
        val pvpNonSpecieId = nonSpecies.find { it.speciesCode == EnumSpecie.PVP.name }?.specieId
        val otherNonSpecieId = nonSpecies.find { it.speciesCode == EnumSpecie.Other.name }?.specieId

        with(mouseRepository.getMiceForOccasion(occasionId)) {
            emit(
                OccasionStats(
                    errorNum = count { mouse -> mouse.speciesID == errorNonSpecieId },
                    closeNum = count { mouse -> mouse.speciesID == closeNonSpecieId },
                    predatorNum = count { mouse -> mouse.speciesID == predatorNonSpecieId },
                    pvpNum = count { mouse -> mouse.speciesID == pvpNonSpecieId },
                    otherNum = count { mouse -> mouse.speciesID == otherNonSpecieId },
                    specieNum = count { mouse -> mouse.speciesID !in nonSpeciesId },
                )
            )
        }
    }

}