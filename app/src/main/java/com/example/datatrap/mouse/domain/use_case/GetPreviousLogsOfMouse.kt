package com.example.datatrap.mouse.domain.use_case

import com.example.datatrap.core.util.EnumSex
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.mouse.data.MouseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.format.DateTimeFormatter

class GetPreviousLogsOfMouse(
    private val mouseRepository: MouseRepository,
    private val localityRepository: LocalityRepository,
) {
    // view previous catches of a mouse
    operator fun invoke(primeMouseID: String): Flow<List<String>> = flow {
        val mouseList = mouseRepository.getMiceByPrimeMouseID(primeMouseID)
            .filter { it.primeMouseID == primeMouseID }
            .sortedBy {
                it.mouseCaught
            }
            .map { mouse ->
                val dateFormated = mouse.mouseCaught.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                val localityName = localityRepository.getLocality(mouse.localityID).localityName
                val sexActive = if (mouse.sexActive == true) "Yes" else "No"

                if (mouse.sex == EnumSex.FEMALE.myName) {
                    val gravidity = if (mouse.gravidity == true) "Yes" else "No"
                    val lactating = if (mouse.lactating == true) "Yes" else "No"
                    "Catch DateTime - $dateFormated, Locality - ${localityName}, Trap Number - ${mouse.trapID}, Weight - ${mouse.weight}, Gravidity - $gravidity, Lactating - $lactating, Sex. Active - $sexActive"
                } else {
                    "Catch DateTime - $dateFormated, Locality - ${localityName}, Trap Number - ${mouse.trapID}, Weight - ${mouse.weight}, Sex. Active - $sexActive"
                }
            }

        emit(mouseList)
    }
}