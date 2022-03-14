package com.example.datatrap.mouse.fragments.generator

import android.util.Log
import com.example.datatrap.myenums.EnumTeam

class CodeGenerator(
    private var code: Int,
    private val fingers: Int,
    private val team: Int?,
    private var codeList: List<Int>
) {

    private val codeRange = 1..9999
    private val badRangeFor4 = arrayOf(500..599, 900..999, 5000..5999, 9000..9999).toList()
    private var isCycle2 = 0

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

    private fun checkNumberEvenOrOddForTeam(team: Int) {
        if (isCycle2 < 2) {
            if (team == EnumTeam.EVEN_TEAM.numTeam) {
                if (code % 2 == 0) {
                    Log.d("CodeGenerator", "Generation Complete")
                } else {
                    code += 1
                    checkCodeRange()
                    checkCodeFreeInLocality()
                    if (has4Fingers(fingers)) {
                        checkBadRangesFor4Fingers()
                    }
                    checkNumberEvenOrOddForTeam(team)
                }
            } else if (team == EnumTeam.ODD_TEAM.numTeam) {
                if (code % 2 != 0) {
                    Log.d("CodeGenerator", "Generation Complete")
                } else {
                    code += 1
                    checkCodeRange()
                    checkCodeFreeInLocality()
                    if (has4Fingers(fingers)) {
                        checkBadRangesFor4Fingers()
                    }
                    checkNumberEvenOrOddForTeam(team)
                }
            }
        }
    }

    fun generateCode(): Int {
        checkCodeRange()
        checkCodeFreeInLocality()
        if (has4Fingers(fingers)) {
            checkBadRangesFor4Fingers()
        }
        checkNumberEvenOrOddForTeam(team!!)
        if (isCycle2 < 2) {
            return code
        }
        return 0
    }

}
