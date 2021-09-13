package com.example.datatrap.mouse.fragments.generator

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.datatrap.models.Mouse
import com.example.datatrap.viewmodels.MouseViewModel
import java.util.*

class CodeGenerator(fragment: Fragment, private var code: Int, private val fingers: Int, private val team: Int?, localityId: Long) {

    private val mouseViewModel = ViewModelProvider(fragment).get(MouseViewModel::class.java)
    private val mouseList: MutableList<Mouse> = mouseViewModel.getOldMiceForLocality(localityId).value!!.toMutableList()
    // list obsadenych ID mysi
    private val activeMouseList: List<Mouse> = mouseList.filter {
        // v liste ostanu len zaznami mladsie ako dva roky
        //63158400000
        //63072000000 // vsetko toto je trvanie dvoch rokov preco to ma dve hodnoty
        //63158400000
        //63072000000
        (Calendar.getInstance().time.time - it.catchDateTime.time) <= 63072000000
    }
    private val codeRange = 1..9999
    private val badRangeFor4 = arrayOf(500..599, 900..999, 5000..5999, 9000..9999).toList()
    private var isCycle2 = 0

    private fun has4Fingers(f: Int): Boolean{
        return (f == 4)
    }

    private fun isInCodeRange(code: Int): Boolean{
        return (code in codeRange)
    }
    private fun checkCodeRange(){
        if (!isInCodeRange(code) && isCycle2 < 2){
            // ak code nie je v intervale tak
            code = 1
            isCycle2 += 1
        }
    }

    private fun isCodeFreeInLocality(code: Int): Boolean{
        // volny bude vtedy ak datetime je starsi ako 2 roky
        // a potom treba pozriet ci ma recapture 1
        activeMouseList.forEach{
            if (it.code == code){
                return true
            }
        }
        return false
    }
    private fun checkCodeFreeInLocality(){
        if (isCycle2 < 2){
            val oldCode = code
            while (isCodeFreeInLocality(code)){
                code += 1
            }
            if (oldCode != code){
                checkCodeRange()
                checkCodeFreeInLocality()
            }
        }
    }

    private fun checkBadRangesFor4Fingers(){
        if (isCycle2 < 2){
            val oldCode = code

            run myLoop@{
                badRangeFor4.forEach {
                    if (code in it){
                        code = it.last + 1
                        return@myLoop
                    }
                }
            }

            if (oldCode != code){
                checkCodeRange()
                checkCodeFreeInLocality()
                checkBadRangesFor4Fingers()
            }
        }
    }

    private fun checkNumberEvenOrOddForTeam(team: Int){
        if (isCycle2 < 2){
            if (team == 0){
                if (code % 2 == 0){
                    Log.d("CodeGenerator", "Generation Complete")
                }else{
                    code += 1
                    checkCodeRange()
                    checkCodeFreeInLocality()
                    if (has4Fingers(fingers)){
                        checkBadRangesFor4Fingers()
                    }
                    checkNumberEvenOrOddForTeam(team)
                }
            }else{
                if (code % 2 != 0){
                    Log.d("CodeGenerator", "Generation Complete")
                }else{
                    code += 1
                    checkCodeRange()
                    checkCodeFreeInLocality()
                    if (has4Fingers(fingers)){
                        checkBadRangesFor4Fingers()
                    }
                    checkNumberEvenOrOddForTeam(team)
                }
            }
        }
    }

    fun generateCode(): Int{
        checkCodeRange()
        checkCodeFreeInLocality()
        if (has4Fingers(fingers)){
            checkBadRangesFor4Fingers()
        }
        checkNumberEvenOrOddForTeam(team!!)
        if (isCycle2 < 2){
            return code
        }
        return 0
    }

}
