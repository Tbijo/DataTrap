package com.example.datatrap.occasion.domain.use_case

import com.example.datatrap.core.util.EnumSpecie
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.occasion.domain.model.OccasionStats
import com.example.datatrap.specie.data.SpecieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CountSpecialSpeciesUseCase(
    private val specieRepository: SpecieRepository,
    private val mouseRepository: MouseRepository,
) {
    suspend operator fun invoke(occasionId: String): Flow<OccasionStats> {
        val nonSpecies = specieRepository.getNonSpecie()
        val nonSpeciesId = nonSpecies.map { it.specieId }

        val errorNonSpecieId = nonSpecies.find { it.speciesCode == EnumSpecie.TRE.name }?.specieId
        val closeNonSpecieId = nonSpecies.find { it.speciesCode == EnumSpecie.C.name }?.specieId
        val predatorNonSpecieId = nonSpecies.find { it.speciesCode == EnumSpecie.P.name }?.specieId
        val pvpNonSpecieId = nonSpecies.find { it.speciesCode == EnumSpecie.PVP.name }?.specieId
        val otherNonSpecieId = nonSpecies.find { it.speciesCode == EnumSpecie.Other.name }?.specieId

        return mouseRepository.getMiceForOccasion(occasionId).map {
            OccasionStats(
                errorNum = it.count { mouse -> mouse.speciesID == errorNonSpecieId },
                closeNum = it.count { mouse -> mouse.speciesID == closeNonSpecieId },
                predatorNum = it.count { mouse -> mouse.speciesID == predatorNonSpecieId },
                pvpNum = it.count { mouse -> mouse.speciesID == pvpNonSpecieId },
                otherNum = it.count { mouse -> mouse.speciesID == otherNonSpecieId },
                specieNum = it.count { mouse -> mouse.speciesID !in nonSpeciesId },
            )
        }
    }

}