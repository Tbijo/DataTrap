package com.example.datatrap.occasion.presentation.occasion_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.datatrap.core.util.EnumSpecie
import com.example.datatrap.mouse.presentation.mouse_list.MouseListViewModel
import com.example.datatrap.occasion.domain.model.OccasionView
import com.example.datatrap.occasion.presentation.occasion_list.OccasionListViewModel
import com.example.datatrap.core.presentation.components.ViewImageFragment
import java.text.SimpleDateFormat
import java.util.Date

class OccasionDetailViewModel: ViewModel() {

    private val occasionListViewModel: OccasionListViewModel by viewModels()
    private val occasionImageViewModel: OccasionImageViewModel by viewModels()
    private val mouseListViewModel: MouseListViewModel by viewModels()

    private var path: String? = null

    init {
        mouseListViewModel.getMiceForOccasion(args.occasionId).observe(viewLifecycleOwner) { miceList ->
            val specieCodeStr = EnumSpecie.values().map {
                it.name
            }
            var numError = 0
            var numClose = 0
            var numPredator = 0
            var numPVP = 0
            var numOther = 0
            val setSpecie = mutableSetOf<String>()
            miceList.forEach {
                when(it.specieCode) {
                    EnumSpecie.TRE.name -> numError++
                    EnumSpecie.C.name -> numClose++
                    EnumSpecie.P.name -> numPredator++
                    EnumSpecie.PVP.name -> numPVP++
                    EnumSpecie.Other.name -> numOther++
                }
                if (it.specieCode !in specieCodeStr) setSpecie.add(it.specieCode)
            }
            binding.tvErrorNum.text = numError.toString()
            binding.tvCloseNum.text = numClose.toString()
            binding.tvPredatorNum.text = numPredator.toString()
            binding.tvPvpNum.text = numPVP.toString()
            binding.tvOtherNum.text = numOther.toString()
            binding.tvSpecieNum.text = setSpecie.size.toString()
        }

        occasionListViewModel.getOccasionView(args.occasionId).observe(viewLifecycleOwner) {
            if (it != null) {
                initOccasionValuesToView(it)
            }
        }

        occasionImageViewModel.getImageForOccasion(args.occasionId).observe(viewLifecycleOwner) {
            if (it != null) {
                binding.ivOccImage.setImageURI(it.path.toUri())
                path = it.path
            }
        }

        binding.ivOccImage.setOnClickListener {
            if (path != null) {
                val fragman = requireActivity().supportFragmentManager
                val floatFrag = ViewImageFragment(path!!)
                floatFrag.show(fragman, "FloatFragViewImage")
            }
        }
    }

    private fun initOccasionValuesToView(occasion: OccasionView) {
        binding.tvOccNum.text = occasion.occasionNum.toString()
        binding.tvLocalityName.text = occasion.locality
        binding.tvMethod.text = occasion.method
        binding.tvMethodType.text = occasion.methodType
        binding.tvTrapType.text = occasion.trapType
        binding.tvEnvType.text = occasion.envType.toString()
        binding.tvVegType.text = occasion.vegetType.toString()
        binding.tvOccDatetime.text = SimpleDateFormat.getDateTimeInstance().format(Date(occasion.occasionStart))
        binding.tvGotCaught.text = if (occasion.gotCaught == true) "Yes" else "No"
        binding.tvOccNumTraps.text = occasion.numTraps.toString()
        binding.tvOccNumMice.text = occasion.numMice.toString()
        binding.tvTemp.text = occasion.temperature.toString()
        binding.tvWeather.text = occasion.weather.toString()
        binding.tvLeg.text = occasion.leg
        binding.tvOccNote.text = occasion.note.toString()
    }

    fun getOccasionView(occasionId: Long): LiveData<OccasionView> {
        return occasionRepository.getOccasionView(occasionId)
    }
}