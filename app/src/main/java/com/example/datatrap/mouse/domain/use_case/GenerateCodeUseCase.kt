package com.example.datatrap.mouse.domain.use_case

import com.example.datatrap.core.data.pref.PrefRepository
import com.example.datatrap.core.util.Constants
import com.example.datatrap.core.util.EnumCaptureID
import com.example.datatrap.core.util.EnumSpecie
import com.example.datatrap.core.util.Resource
import com.example.datatrap.core.util.ScienceTeam
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.mouse.domain.util.CodeGeneratorError
import com.example.datatrap.mouse.domain.util.CodeGeneratorException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.ZonedDateTime

class GenerateCodeUseCase(
    private val mouseRepository: MouseRepository,
    private val prefRepository: PrefRepository,
) {
    private val codeRange = 1..9999
    private val badRangeFor4 = arrayOf(500..599, 900..999, 5000..5999, 9000..9999).toList()
    private var isCycle2 = 0

    private var code: Int = 0
    private var team: Int = 0
    private lateinit var codeList: List<Int>

    private fun has4Fingers(f: Int): Boolean {
        return (f == 4)
    }

    private fun isInCodeRange(code: Int): Boolean {
        return (code in codeRange)
    }

    private fun checkCodeRange() {
        if (!isInCodeRange(code) && isCycle2 < 2) {
            // ak code nie je v intervale tak
            code = 1
            isCycle2 += 1
        }
    }

    private fun isCodeFreeInLocality(code: Int): Boolean {
        // volny bude vtedy ak datetime je starsi ako 2 roky
        codeList.forEach {
            if (it == code) {
                return true
            }
        }
        return false
    }

    private fun checkCodeFreeInLocality() {
        if (isCycle2 < 2) {
            val oldCode = code
            while (isCodeFreeInLocality(code)) {
                code += 1
            }
            if (oldCode != code) {
                checkCodeRange()
                checkCodeFreeInLocality()
            }
        }
    }

    private fun checkBadRangesFor4Fingers() {
        if (isCycle2 < 2) {
            val oldCode = code

            run myLoop@{
                badRangeFor4.forEach {
                    if (code in it) {
                        code = it.last + 1
                        return@myLoop
                    }
                }
            }

            if (oldCode != code) {
                checkCodeRange()
                checkCodeFreeInLocality()
                checkBadRangesFor4Fingers()
            }
        }
    }

    private fun checkNumberEvenOrOddForTeam(team: Int, fingers: Int) {
        if (isCycle2 < 2) {
            if (team == ScienceTeam.EVEN_TEAM.numTeam) {
                if (code % 2 == 0) {
                    println("CodeGenerator - Generation Complete")
                } else {
                    code += 1
                    checkCodeRange()
                    checkCodeFreeInLocality()
                    if (has4Fingers(fingers)) {
                        checkBadRangesFor4Fingers()
                    }
                    checkNumberEvenOrOddForTeam(team, fingers)
                }
            } else if (team == ScienceTeam.ODD_TEAM.numTeam) {
                if (code % 2 != 0) {
                    println("CodeGenerator - Generation Complete")
                } else {
                    code += 1
                    checkCodeRange()
                    checkCodeFreeInLocality()
                    if (has4Fingers(fingers)) {
                        checkBadRangesFor4Fingers()
                    }
                    checkNumberEvenOrOddForTeam(team, fingers)
                }
            }
        }
    }

    private fun startGenerating(fingers: Int): Int {
        checkCodeRange()
        checkCodeFreeInLocality()
        if (has4Fingers(fingers)) {
            checkBadRangesFor4Fingers()
        }
        checkNumberEvenOrOddForTeam(team, fingers)
        if (isCycle2 < 2) {
            return code
        }
        return 0
    }

    operator fun invoke(
        upperFingers: Int?, specieCode: String?, captureID: String?, localityId: String?,
    ): Flow<Resource<Int>> = flow {
        try {
            if (upperFingers == null) throw CodeGeneratorException(CodeGeneratorError.NUMBER_OF_FINGERS_MISSING)

            if (localityId == null) throw CodeGeneratorException(CodeGeneratorError.LOCALITY_ID_MISSING)

            if (specieCode == null || specieCode == EnumSpecie.Other.name || specieCode == EnumSpecie.PVP.name ||
                specieCode == EnumSpecie.TRE.name || specieCode == EnumSpecie.C.name || specieCode == EnumSpecie.P.name
            ) {
                throw CodeGeneratorException(CodeGeneratorError.INVALID_SPECIE_CODE)
            }

            if (captureID == null || captureID == EnumCaptureID.ESCAPED.myName ||
                captureID == EnumCaptureID.DIED.myName
            ) {
                throw CodeGeneratorException(CodeGeneratorError.INVALID_CAPTURE_ID)
            }

            code = (getMiceFromLocality(localityId) + 1)

            prefRepository.readUserTeam().collect { teamNum ->
                teamNum?.let {
                    team = teamNum
                }
            }

            codeList = getCodeOfAliveMiceInLocality(localityId)

            val code = startGenerating(upperFingers)

            emit(Resource.Success(code))
        } catch (e: CodeGeneratorException) {
            emit(Resource.Error(e))
        }
    }

    // number of mice in locality
    private suspend fun getMiceFromLocality(localityId: String): Int {
        return mouseRepository.getMice().count {
            it.localityID == localityId && it.code != null && it.recapture == false
        }
    }

    // list of codes which are not older than 2 years (meaning rats are alive)
    private suspend fun getCodeOfAliveMiceInLocality(localityId: String): List<Int> {
        val currentTime = ZonedDateTime.now().toEpochSecond()

        val codeList = mouseRepository.getMice()
            .filter {
                it.localityID == localityId && it.code != null && ((currentTime - it.mouseCaught.toEpochSecond()) < Constants.SECONDS_IN_2_YEAR)
            }
            .mapNotNull { it.code }

        return codeList
    }

}
