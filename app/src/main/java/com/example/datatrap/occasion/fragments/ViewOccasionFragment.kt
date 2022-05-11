package com.example.datatrap.occasion.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.datatrap.databinding.FragmentViewOccasionBinding
import com.example.datatrap.models.Mouse
import com.example.datatrap.models.tuples.OccasionView
import com.example.datatrap.models.tuples.SpecList
import com.example.datatrap.myenums.EnumSpecie
import com.example.datatrap.picture.fragments.ViewImageFragment
import com.example.datatrap.viewmodels.MouseViewModel
import com.example.datatrap.viewmodels.OccasionImageViewModel
import com.example.datatrap.viewmodels.OccasionViewModel
import com.example.datatrap.viewmodels.SpecieViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class ViewOccasionFragment : Fragment() {

    private var _binding: FragmentViewOccasionBinding? = null
    private val binding get() = _binding!!

    private val occasionViewModel: OccasionViewModel by viewModels()
    private val occasionImageViewModel: OccasionImageViewModel by viewModels()
    private val mouseViewModel: MouseViewModel by viewModels()

    private val args by navArgs<ViewOccasionFragmentArgs>()

    private var path: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = FragmentViewOccasionBinding.inflate(inflater, container, false)

        mouseViewModel.getMiceForOccasion(args.occasionId).observe(viewLifecycleOwner) { miceList ->
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

        occasionViewModel.getOccasionView(args.occasionId).observe(viewLifecycleOwner) {
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

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initOccasionValuesToView(occasion: OccasionView) {
        binding.tvOccNum.text = occasion.occasionNum.toString()
        binding.tvLocalityName.text = occasion.locality
        binding.tvMethod.text = occasion.method
        binding.tvMethodType.text = occasion.methodType
        binding.tvTrapType.text = occasion.trapType
        binding.tvEnvType.text = occasion.envType.toString()
        binding.tvVegType.text = occasion.vegetType.toString()
        binding.tvOccDatetime.text = SimpleDateFormat.getDateTimeInstance().format(occasion.dateTime)
        binding.tvGotCaught.text = if (occasion.gotCaught == true) "Yes" else "No"
        binding.tvOccNumTraps.text = occasion.numTraps.toString()
        binding.tvOccNumMice.text = occasion.numMice.toString()
        binding.tvTemp.text = occasion.temperature.toString()
        binding.tvWeather.text = occasion.weather.toString()
        binding.tvLeg.text = occasion.leg
        binding.tvOccNote.text = occasion.note.toString()
    }

}