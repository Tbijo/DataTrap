package com.example.datatrap.specie.presentation.specie_add_edit

import android.app.AlertDialog
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.datatrap.R
import com.example.datatrap.specie.data.specie_image.SpecieImage
import com.example.datatrap.specie.data.Specie
import java.util.Calendar

class SpecieViewModel: ViewModel() {

    private var upperFingers: Int? = null
    private var specieId: Long = 0
    // private val specieImageViewModel: SpecieImageViewModel by viewModels()
    private var upperFingers: Int? = null
    private lateinit var currentSpecie: Specie
    private var specieImage: SpecieImage? = null

    init {
        binding.rgUpperFingers.setOnCheckedChangeListener { _, radioButtonId ->
            when (radioButtonId) {
                binding.rb4.id -> upperFingers = 4
                binding.rb5.id -> upperFingers = 5
                binding.rbFingerNull.id -> upperFingers = null
            }
        }

        when (item.itemId) {
            R.id.menu_save -> insertSpecie()
            R.id.menu_camera -> goToCamera()
        }

        // UPDATE
        binding.rgUpperFingers.setOnCheckedChangeListener { _, radioButtonId ->
            when (radioButtonId){
                binding.rb4.id -> upperFingers = 4
                binding.rb5.id -> upperFingers = 5
                binding.rbFingerNull.id -> upperFingers = null
            }
        }

        specieListViewModel.getSpecie(args.specList.specieId).observe(viewLifecycleOwner) {
            currentSpecie = it
            initSpecieValuesToView(it)
        }

        specieImageViewModel.getImageForSpecie(args.specList.specieId).observe(viewLifecycleOwner) {
            specieImage = it
        }
        when(item.itemId){
            R.id.menu_save -> updateSpecie()
            R.id.menu_camera -> goToCamera()
            R.id.menu_delete -> deleteSpecie()
        }

    }

    private fun goToCamera() {
        if (specieId <= 0) {
            Toast.makeText(
                requireContext(),
                "You need to insert a specie first.",
                Toast.LENGTH_LONG
            )
                .show()
            return
        }

        val action =
            AddSpecieFragmentDirections.actionAddSpecieFragmentToGetPictureSpecieFragment(specieId)
        findNavController().navigate(action)
    }

    private fun insertSpecie() {
        if (specieId > 0) {
            Toast.makeText(requireContext(), "Specie already inserted.", Toast.LENGTH_LONG).show()
            return
        }

        val speciesCode = binding.etSpeciesCode.text.toString()
        val fullName = binding.etFullName.text.toString()

        if (checkInput(speciesCode, fullName)) {
            val authority = binding.etAuthority.text.toString().ifBlank { null }
            val synonym = binding.etSynonym.text.toString().ifBlank { null }
            val description = binding.etDescription.text.toString().ifBlank { null }
            val isSmallMammal: Boolean = binding.cbIsSmallMammal.isChecked
            val minWeight = giveOutPutFloat(binding.etMinWeight.text.toString())
            val maxWeight = giveOutPutFloat(binding.etMaxWeight.text.toString())

            val bodyLen = giveOutPutFloat(binding.etBodyLen.text.toString())
            val tailLen = giveOutPutFloat(binding.etTailLen.text.toString())
            val feetMinLen = giveOutPutFloat(binding.etMinFeet.text.toString())
            val feetMaxLen = giveOutPutFloat(binding.etMaxFeet.text.toString())

            val note = binding.etNote.text.toString().ifBlank { null }

            val specie = Specie(
                0,
                speciesCode,
                fullName,
                synonym,
                authority,
                description,
                isSmallMammal,
                upperFingers,
                minWeight,
                maxWeight,
                bodyLen,
                tailLen,
                feetMinLen,
                feetMaxLen,
                note,
                Calendar.getInstance().time,
                null
            )

            specieListViewModel.insertSpecie(specie)

            Toast.makeText(requireContext(), "Specie Added.", Toast.LENGTH_SHORT).show()

            specieListViewModel.specieId.observe(viewLifecycleOwner) {
                specieId = it
            }
        } else {
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun checkInput(speciesCode: String, fullName: String): Boolean {
        return speciesCode.isNotEmpty() && fullName.isNotEmpty()
    }

    private fun giveOutPutFloat(input: String?): Float? {
        return if (input.isNullOrBlank() || input == "null") null else input.toFloat()
    }

    private fun goToCamera() {
        val action = UpdateSpecieFragmentDirections.actionUpdateSpecieFragmentToGetPictureSpecieFragment(
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

            specieListViewModel.deleteSpecie(args.specList.specieId, specieImage?.path)

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

            specieListViewModel.updateSpecie(specie)

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