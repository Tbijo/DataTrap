package com.example.datatrap.mouse.domain.use_case

import com.example.datatrap.mouse.data.MouseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetOccupiedTrapIdsInOccasion(
    private val mouseRepository: MouseRepository,
) {
    // numbers of traps currently occupied

    suspend operator fun invoke(occasionId: String): Flow<List<Int>> {
        return mouseRepository.getMiceForOccasion(occasionId).map { mice ->
            mice.mapNotNull { mouse ->
                mouse.trapID
            }
        }
    }
}