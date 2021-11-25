package com.example.datatrap.occasion.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.datatrap.databinding.FragmentViewOccasionBinding
import com.example.datatrap.models.tuples.OccasionView
import com.example.datatrap.picture.fragments.ViewImageFragment
import com.example.datatrap.viewmodels.OccasionImageViewModel
import com.example.datatrap.viewmodels.OccasionViewModel
import java.text.SimpleDateFormat

class ViewOccasionFragment : Fragment() {

    private var _binding: FragmentViewOccasionBinding? = null
    private val binding get() = _binding!!
    private lateinit var occasionViewModel: OccasionViewModel
    private lateinit var occasionImageViewModel: OccasionImageViewModel
    private val args by navArgs<ViewOccasionFragmentArgs>()

    private var path: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        occasionViewModel = ViewModelProvider(this).get(OccasionViewModel::class.java)
        occasionImageViewModel = ViewModelProvider(this).get(OccasionImageViewModel::class.java)

        _binding = FragmentViewOccasionBinding.inflate(inflater, container, false)

        occasionViewModel.getOccasionView(args.occasionId).observe(viewLifecycleOwner, {
            initOccasionValuesToView(it)
        })

        occasionImageViewModel.getImageForOccasion(args.occasionId).observe(viewLifecycleOwner, {
            if (it != null) {
                binding.ivOccImage.setImageURI(it.path.toUri())
                path = it.path
            }
        })

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