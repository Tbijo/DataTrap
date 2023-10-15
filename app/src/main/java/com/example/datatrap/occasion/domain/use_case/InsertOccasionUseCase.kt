package com.example.datatrap.occasion.domain.use_case

import com.example.datatrap.session.data.SessionRepository
import java.time.ZonedDateTime

class InsertOccasionUseCase(
    private val sessionRepository: SessionRepository,
) {
    // Insert Occasion, po pridani update Session numOcc + 1

    suspend operator fun invoke(sessionID: String) {
        val session = sessionRepository.getSession(sessionID)
        sessionRepository.insertSession(
            session.copy(
                numOcc = session.numOcc + 1,
                sessionDateTimeUpdated = ZonedDateTime.now(),
            )
        )
    }
}