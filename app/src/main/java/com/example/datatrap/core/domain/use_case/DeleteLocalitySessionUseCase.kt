package com.example.datatrap.core.domain.use_case

import com.example.datatrap.core.data.locality_session.LocalitySessionCrossRef
import com.example.datatrap.core.data.locality_session.LocalitySessionRepository
import com.example.datatrap.locality.data.locality.LocalityRepository
import java.time.ZonedDateTime

class DeleteLocalitySessionUseCase(
    private val localitySessionRepository: LocalitySessionRepository,
    private val localityRepository: LocalityRepository,
) {

    // DELETE LocSessCrossRef, po vymazani update Locality numSession - 1

    suspend operator fun invoke(localityId: String, sessionId: String) {
        val localitySessionCrossRef = LocalitySessionCrossRef(localityId, sessionId)
        localitySessionRepository.deleteLocalitySessionCrossRef(localitySessionCrossRef)

        // update locality
        val locality = localityRepository.getLocality(localityId)
        localityRepository.insertLocality(
            locality.copy(
                numSessions = locality.numSessions - 1,
                localityDateTimeUpdated = ZonedDateTime.now(),
            )
        )
    }

}