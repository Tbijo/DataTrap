package com.example.datatrap.mouse.domain.use_case

import com.example.datatrap.mouse.data.MouseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetOccupiedTrapIdsInOccasion(
    private val mouseRepository: MouseRepository,
) {
    // numbers of traps currently occupied
    operator fun invoke(occasionId: String): Flow<List<Int>> = flow {
        val trapList = mouseRepository.getMiceForOccasion(occasionId)
            .mapNotNull {
                it.trapID
            }
        emit(trapList)
    }
}