package com.example.datatrap.specie.fragments

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
import com.example.datatrap.databinding.FragmentUpdateSpecieBinding
import com.example.datatrap.models.Specie
import com.example.datatrap.viewmodels.SpecieViewModel

class UpdateSpecieFragment : Fragment() {
    /*Treba dokoncit pridavanie fotiek*/
    /*Vybrat jednu fotku z databazy a zobrazit nazov*/

    private var _binding: FragmentUpdateSpecieBinding? = null
    private val binding get() = _binding!!
    private lateinit var specieViewModel: SpecieViewModel
    private val args by navArgs<UpdateSpecieFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateSpecieBinding.inflate(inflater, container, false)
        specieViewModel = ViewModelProvider(this).get(SpecieViewModel::class.java)

        binding.etSpeciesCode.setText(args.specie.speciesCode)
        binding.etFullName.setText(args.specie.fullName)
        binding.etAuthority.setText(args.specie.authority)
        binding.etDescription.setText(args.specie.description)
        binding.etSynonym.setText(args.specie.synonym)
        binding.etUpperFingers.setText(args.specie.upperFingers.toString())
        binding.etMaxWeight.setText(args.specie.maxWeight.toString())
        binding.etMinWeight.setText(args.specie.maxWeight.toString())
        binding.cbIsSmallMammal.isChecked = args.specie.isSmallMammal == 1
        binding.etNote.setText(args.specie.note)

        /*Vybrat jednu fotku z databazy a zobrazit nazov*/
        binding.tvPicture.text = args.specie.img.toString()

        binding.btnPicture.setOnClickListener {
            addPicture()
        }

        binding.btnAddSpecie.setOnClickListener {
            updateSpecie()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateSpecie() {
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

            specieViewModel.updateSpecie(specie)

            Toast.makeText(requireContext(), "Specie Updated.", Toast.LENGTH_SHORT).show()

            val action = UpdateSpecieFragmentDirections.actionUpdateSpecieFragmentToListSpecieFragment()
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

    private fun addPicture() {
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        try {
//            registerForActivityResult(takePictureIntent, 1)
//        } catch (e: ActivityNotFoundException) {
//            Toast.makeText(requireContext(), "Camera is not working", Toast.LENGTH_LONG).show()
//        }
    }

}