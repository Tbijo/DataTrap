package com.example.datatrap.mouse.domain.use_case

import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.mouse.domain.model.MouseListData
import com.example.datatrap.specie.data.SpecieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.format.DateTimeFormatter

class GetMiceByOccasion(
    private val mouseRepository: MouseRepository,
    private val specieRepository: SpecieRepository,
) {
    operator fun invoke(occasionID: String): Flow<List<MouseListData>> = flow {
        val specieList = specieRepository.getSpecies()
        val mappedData = mouseRepository.getMiceForOccasion(occasionID).map {
            MouseListData(
                mouseId = it.mouseId,
                primeMouseID = it.primeMouseID,
                mouseCode = it.code,
                specieCode = specieList.find { specie -> specie.specieId == it.speciesID }?.speciesCode ?: "NONE",
                mouseCaught = it.mouseCaught.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                sex = it.sex,
            )
        }

        emit(mappedData)
    }
}