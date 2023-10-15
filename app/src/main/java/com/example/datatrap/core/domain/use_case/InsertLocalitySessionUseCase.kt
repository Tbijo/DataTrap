package com.example.datatrap.core.domain.use_case

import com.example.datatrap.core.data.locality_session.LocalitySessionCrossRef
import com.example.datatrap.core.data.locality_session.LocalitySessionRepository
import com.example.datatrap.locality.data.locality.LocalityRepository
import java.time.ZonedDateTime

/*
* Create DAO and Entity for Locality and Session
* Every time new Combination is made num of sessions in a locality goes up and vice versa
* A Combination is created only if it does not exist
* The numSes in a Locality goes up only when the combination does not exist
*/
// INSERT LocSessCrossRef, po pridani update Locality numSession + 1

class InsertLocalitySessionUseCase(
    private val localitySessionRepository: LocalitySessionRepository,
    private val localityRepository: LocalityRepository,
) {
    suspend operator fun invoke(localityId: String, sessionId: String) {
        val existsComb = localitySessionRepository.existsLocalSessCrossRef(
            localityId, sessionId,
        )

        if (existsComb) return

        // create combination
        val localitySessionCrossRef = LocalitySessionCrossRef(
            localityId, sessionId
        )
        localitySessionRepository.insertLocalitySessionCrossRef(localitySessionCrossRef)

        // update locality
        val locality = localityRepository.getLocality(localityId)
        localityRepository.insertLocality(
            locality.copy(
                numSessions = locality.numSessions + 1,
                localityDateTimeUpdated = ZonedDateTime.now(),
            )
        )
    }
}