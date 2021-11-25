package com.example.datatrap.specie.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentUpdateSpecieBinding
import com.example.datatrap.models.Specie
import com.example.datatrap.models.SpecieImage
import com.example.datatrap.viewmodels.SpecieImageViewModel
import com.example.datatrap.viewmodels.SpecieViewModel
import java.io.File
import java.util.*

class UpdateSpecieFragment : Fragment() {

    private var _binding: FragmentUpdateSpecieBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateSpecieFragmentArgs>()
    private lateinit var specieViewModel: SpecieViewModel
    private lateinit var specieImageViewModel: SpecieImageViewModel

    private var upperFingers: Int? = null
    private lateinit var currentSpecie: Specie
    private var specieImage: SpecieImage? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateSpecieBinding.inflate(inflater, container, false)
        specieViewModel = ViewModelProvider(this).get(SpecieViewModel::class.java)
        specieImageViewModel = ViewModelProvider(this).get(SpecieImageViewModel::class.java)

        binding.rgUpperFingers.setOnCheckedChangeListener { _, radioButtonId ->
            when (radioButtonId){
                binding.rb4.id -> upperFingers = 4
                binding.rb5.id -> upperFingers = 5
                binding.rbFingerNull.id -> upperFingers = null
            }
        }

        specieViewModel.getSpecie(args.specList.specieId).observe(viewLifecycleOwner, {
            currentSpecie = it
            initSpecieValuesToView(it)
        })

        specieImageViewModel.getImageForSpecie(args.specList.specieId).observe(viewLifecycleOwner, {
            specieImage = it
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.specie_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> updateSpecie()
            R.id.menu_camera -> goToCamera()
            R.id.menu_delete -> deleteSpecie()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun deleteImage() {
        if (specieImage != null) {
            // odstranit fyzicku zlozku
            val myFile = File(specieImage!!.path)
            if (myFile.exists()) myFile.delete()
        }
    }

    private fun goToCamera() {
        val action = UpdateSpecieFragmentDirections.actionUpdateSpecieFragmentToGetPictureFragment(
            args.specList.specieId
        )
        findNavController().navigate(action)
    }

    private fun initSpecieValuesToView(specie: Specie) {
        binding.etSpeciesCode.setText(specie.speciesCode)
        binding.etFullName.setText(specie.fullName)
        binding.etAuthority.setText(specie.authority.toString())
        binding.etDescription.setText(specie.description.toString())
        binding.etSynonym.setText(specie.synonym.toString())

        when(specie.upperFingers){
            4 -> binding.rb4.isChecked = true
            5 -> binding.rb5.isChecked = true
        }

        binding.etMaxWeight.setText(specie.maxWeight.toString())
        binding.etMinWeight.setText(specie.maxWeight.toString())

        binding.etUpBodyLen.setText(specie.bodyLength.toString())
        binding.etUpTailLen.setText(specie.tailLength.toString())
        binding.etUpMinFeet.setText(specie.feetLengthMin.toString())
        binding.etUpFeetMax.setText(specie.feetLengthMax.toString())

        binding.cbIsSmallMammal.isChecked = specie.isSmallMammal
        binding.etNote.setText(specie.note.toString())
    }

    private fun deleteSpecie() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            deleteImage()

            specieViewModel.deleteSpecie(args.specList.specieId)

            Toast.makeText(requireContext(),"Specie deleted.", Toast.LENGTH_LONG).show()

            findNavController().navigateUp()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete Specie?")
            .setMessage("Are you sure you want to delete this specie?")
            .create().show()
    }

    private fun updateSpecie() {
        val speciesCode = binding.etSpeciesCode.text.toString()
        val fullName = binding.etFullName.text.toString()

        if (checkInput(speciesCode, fullName)){

            val specie: Specie = currentSpecie.copy()
            specie.speciesCode = speciesCode
            specie.fullName = fullName
            specie.authority = if (isTextNull(binding.etAuthority.text.toString())) null else binding.etAuthority.text.toString()
            specie.synonym = if (isTextNull(binding.etSynonym.text.toString())) null else binding.etSynonym.text.toString()
            specie.description = if (isTextNull(binding.etDescription.text.toString())) null else binding.etDescription.text.toString()
            specie.isSmallMammal = binding.cbIsSmallMammal.isChecked
            specie.upperFingers = upperFingers
            specie.minWeight = giveOutPutFloat(binding.etMinWeight.text.toString())
            specie.maxWeight = giveOutPutFloat(binding.etMaxWeight.text.toString())

            specie.bodyLength = giveOutPutFloat(binding.etUpBodyLen.text.toString())
            specie.tailLength = giveOutPutFloat(binding.etUpTailLen.text.toString())
            specie.feetLengthMin = giveOutPutFloat(binding.etUpMinFeet.text.toString())
            specie.feetLengthMax = giveOutPutFloat(binding.etUpFeetMax.text.toString())

            specie.note = if (isTextNull(binding.etNote.text.toString())) null else binding.etNote.text.toString()
            specie.specieDateTimeUpdated = Calendar.getInstance().time

            specieViewModel.updateSpecie(specie)

            Toast.makeText(requireContext(), "Specie Updated.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun isTextNull(string: String): Boolean {
        return string == "null" || string.isBlank()
    }

    private fun checkInput(
        speciesCode: String,
        fullName: String,
    ): Boolean {
        return speciesCode.isNotEmpty() && fullName.isNotEmpty()
    }

    private fun giveOutPutFloat(input: String?): Float?{
        return if (input.isNullOrBlank() || input == "null") null else input.toFloat()
    }

}