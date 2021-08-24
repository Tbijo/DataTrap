package com.example.datatrap.specie.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentAddSpecieBinding
import com.example.datatrap.models.Specie
import com.example.datatrap.viewmodels.SpecieViewModel

class AddSpecieFragment : Fragment() {
/*Treba dokoncit pridavanie fotiek*/
    private var _binding: FragmentAddSpecieBinding? = null
    private val binding get() = _binding!!
    private lateinit var specieViewModel: SpecieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentAddSpecieBinding.inflate(inflater, container, false)
        specieViewModel = ViewModelProvider(this).get(SpecieViewModel::class.java)

        binding.btnAddSpecie.setOnClickListener {
            insertSpecie()
        }

        binding.btnPicture.setOnClickListener {
            addPicture()
        }

        return binding.root
    }

    private fun addPicture() {
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        try {
//            registerForActivityResult(takePictureIntent, 1)
//        } catch (e: ActivityNotFoundException) {
//            Toast.makeText(requireContext(), "Camera is not working", Toast.LENGTH_LONG).show()
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun insertSpecie() {
        val speciesCode = binding.etSpeciesCode.text.toString()
        val fullName = binding.etFullName.text.toString()
        val synonym = binding.etSynonym.text.toString()
        val authority = binding.etAuthority.text.toString()
        val description = binding.etDescription.text.toString()

        val isSmallMammal: Int = if (binding.cbIsSmallMammal.isChecked) 1 else 0

        val upperFingers = binding.etUpperFingers.text.toString()
        val minWeight = binding.etMinWeight.text.toString()
        val maxWeight = binding.etMaxWeight.text.toString()
        val note = binding.etNote.text.toString()
        val img = null

        if (checkInput(speciesCode, fullName, authority)){
            val specie = Specie(speciesCode, fullName, synonym, authority, description, isSmallMammal, Integer.parseInt(upperFingers), Integer.parseInt(minWeight).toFloat(), Integer.parseInt(maxWeight).toFloat(), note, img)

            specieViewModel.insertSpecie(specie)

            Toast.makeText(requireContext(), "Specie Added.", Toast.LENGTH_SHORT).show()

            val action = AddSpecieFragmentDirections.actionAddSpecieFragmentToListSpecieFragment()
            findNavController().navigate(action)
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkInput(
        speciesCode: String,
        fullName: String,
        authority: String,
    ): Boolean {
        return speciesCode.isNotEmpty() && fullName.isNotEmpty() && authority.isNotEmpty()
    }

}