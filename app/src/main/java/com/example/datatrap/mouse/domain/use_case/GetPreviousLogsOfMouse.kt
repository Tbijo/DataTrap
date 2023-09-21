package com.example.datatrap.mouse.domain.use_case

import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.mouse.domain.model.MouseLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.format.DateTimeFormatter

class GetPreviousLogsOfMouse(
    private val mouseRepository: MouseRepository,
    private val localityRepository: LocalityRepository,
) {
    // view previous catches of a mouse
    operator fun invoke(primeMouseID: String): Flow<List<MouseLog>> = flow {
        val mouseList = mouseRepository.getMiceByPrimeMouseID(primeMouseID)
            .filter { it.primeMouseID == primeMouseID }
            .map {
                MouseLog(
                    mouseCaught = it.mouseCaught.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    localityName = localityRepository.getLocality(it.localityID).localityName,
                    trapID = it.trapID,
                    weight = it.weight,
                    sexActive = it.sexActive,
                    gravidity = it.gravidity,
                    lactating = it.lactating,
                )
            }.sortedBy {
                it.mouseCaught
            }

        emit(mouseList)
    }
}