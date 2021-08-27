package com.example.datatrap.occasion.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentAddOccasionBinding
import com.example.datatrap.models.Occasion
import com.example.datatrap.models.Picture
import com.example.datatrap.viewmodels.OccasionViewModel
import com.example.datatrap.viewmodels.PictureViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddOccasionFragment : Fragment() {

    private var _binding: FragmentAddOccasionBinding? = null
    private val binding get() = _binding!!
    private lateinit var occasionViewModel: OccasionViewModel
    private lateinit var pictureViewModel: PictureViewModel
    private val args by navArgs<AddOccasionFragmentArgs>()

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentAddOccasionBinding.inflate(inflater, container, false)
        occasionViewModel = ViewModelProvider(this).get(OccasionViewModel::class.java)
        pictureViewModel = ViewModelProvider(this).get(PictureViewModel::class.java)

        binding.btnAddOccasion.setOnClickListener {
            insertOccasion()
        }

        binding.btnOccPhoto.setOnClickListener {
            takePicture()
        }

        return binding.root
    }

    private fun takePicture() {

    }

    private fun insertOccasion() {
        val occasionNum: Int = Integer.parseInt(binding.etOccasion.text.toString())
        val method: String = binding.spinMethod.selectedItem.toString()
        val methodType: String = binding.spinMethodType.selectedItem.toString()
        val trapType: String = binding.spinTrapType.selectedItem.toString()
        val envType: String? = binding.spinEnvType.selectedItem.toString()
        val vegType: String? = binding.spinVegType.selectedItem.toString()

        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())

        val gotCaught = 0
        val numTraps = 0
        val numMice = 0
        val temperature: Float? = null
        val weather: String? = null
        val leg: String = binding.etLeg.toString()
        val note: String? = binding.etOccasionNote.toString()
        var img: String? = null

        if (checkInput(occasionNum, method, methodType, trapType, leg)){

            if (imageUri != null){
                img = "Nazov Fotky"
                val picture = Picture(img, imageUri.toString(), binding.etOccPicNote.text.toString())
                pictureViewModel.insertPicture(picture)
            }

            val occasion = Occasion(0, occasionNum, args.locality.localityName, args.session.id, method, methodType,
            trapType, envType, vegType, currentDate, gotCaught, numTraps, numMice, temperature,
            weather, leg, note, img)

            occasionViewModel.insertOccasion(occasion)
            Toast.makeText(requireContext(), "New occasion added.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkInput(
        occasion: Int,
        method: String,
        methodType: String,
        trapType: String,
        leg: String
    ): Boolean {
        return occasion.toString().isNotEmpty() && method.isNotEmpty() && methodType.isNotEmpty() && trapType.isNotEmpty() && leg.isNotEmpty()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}