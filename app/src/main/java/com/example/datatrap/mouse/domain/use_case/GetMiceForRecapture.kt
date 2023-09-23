package com.example.datatrap.mouse.domain.use_case

import com.example.datatrap.core.util.Constants
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.mouse.domain.model.MouseRecapList
import com.example.datatrap.specie.data.SpecieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class GetMiceForRecapture(
    private val mouseRepository: MouseRepository,
    private val localityRepository: LocalityRepository,
    private val specieRepository: SpecieRepository,
) {
    // view caught mice (according to their code) for recapture
    // must not be older than 2 years
    operator fun invoke(
        code: Int?, specieID: String?, sex: String?, age: String?, gravidity: Boolean,
        sexActive: Boolean, lactating: Boolean, dateFrom: LocalDate?, dateTo: LocalDate?
    ): Flow<List<MouseRecapList>> = flow {
        val currentTime = ZonedDateTime.now()

        val localTime = LocalTime.of(0, 0, 0)
        val zoneId: ZoneId = ZoneId.systemDefault()
        val zDateFrom = ZonedDateTime.of(dateFrom, localTime, zoneId)
        val zDateTo = ZonedDateTime.of(dateTo, localTime, zoneId)

        val miceForRecap = mouseRepository.getMice()
            .filter {
                it.code == code &&
                it.speciesID == specieID &&
                it.sex == sex &&
                it.age == age &&
                it.gravidity == gravidity &&
                it.lactating == lactating &&
                it.sexActive == sexActive &&
                (it.mouseCaught.isBefore(zDateTo) && it.mouseCaught.isAfter(zDateFrom)) && // TODO check
                (currentTime.toEpochSecond() - it.mouseCaught.toEpochSecond()) < Constants.SECONDS_IN_2_YEAR
            }
            .map {
                MouseRecapList(
                    mouseId = it.mouseId,
                    primeMouseID = it.primeMouseID,
                    code = it.code,
                    age = it.age,
                    weight = it.weight,
                    sex = it.sex,
                    gravidity = it.gravidity,
                    lactating = it.lactating,
                    sexActive = it.sexActive,
                    localityName = localityRepository.getLocality(it.localityID).localityName,
                    specieCode = specieRepository.getSpecie(it.speciesID).speciesCode,
                    mouseCaught = it.mouseCaught.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                )
            }.sortedByDescending { it.mouseCaught }

        emit(miceForRecap)
    }
}