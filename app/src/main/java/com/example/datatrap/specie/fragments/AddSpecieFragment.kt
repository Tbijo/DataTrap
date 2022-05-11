package com.example.datatrap.specie.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentAddSpecieBinding
import com.example.datatrap.models.Specie
import com.example.datatrap.viewmodels.SpecieViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddSpecieFragment : Fragment() {

    private var _binding: FragmentAddSpecieBinding? = null
    private val binding get() = _binding!!

    private val specieViewModel: SpecieViewModel by viewModels()

    private var upperFingers: Int? = null

    private var specieId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentAddSpecieBinding.inflate(inflater, container, false)

        binding.rgUpperFingers.setOnCheckedChangeListener { _, radioButtonId ->
            when (radioButtonId) {
                binding.rb4.id -> upperFingers = 4
                binding.rb5.id -> upperFingers = 5
                binding.rbFingerNull.id -> upperFingers = null
            }
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.specie_menu, menu)
        menu.findItem(R.id.menu_delete).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> insertSpecie()
            R.id.menu_camera -> goToCamera()
        }
        return super.onOptionsItemSelected(item)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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

            specieViewModel.insertSpecie(specie)

            Toast.makeText(requireContext(), "Specie Added.", Toast.LENGTH_SHORT).show()

            specieViewModel.specieId.observe(viewLifecycleOwner) {
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

}