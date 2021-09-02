package com.example.datatrap.specie.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentAddSpecieBinding
import com.example.datatrap.models.Specie
import com.example.datatrap.viewmodels.SharedViewModel
import com.example.datatrap.viewmodels.SpecieViewModel

class AddSpecieFragment : Fragment() {

    private var _binding: FragmentAddSpecieBinding? = null
    private val binding get() = _binding!!
    private lateinit var specieViewModel: SpecieViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private var imgName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentAddSpecieBinding.inflate(inflater, container, false)
        specieViewModel = ViewModelProvider(this).get(SpecieViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.dataToShare.observe(requireActivity(), Observer {
            imgName = it
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.specie_menu, menu)
        menu.findItem(R.id.menu_delete).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> insertSpecie()
            R.id.menu_camera -> goToCamera()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToCamera(){
        val action = AddSpecieFragmentDirections.actionAddSpecieFragmentToGetPictureFragment(null)
        findNavController().navigate(action)
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

        if (checkInput(speciesCode, fullName, authority)){

            val specie = Specie(0, speciesCode, fullName, synonym, authority, description, isSmallMammal, Integer.parseInt(upperFingers), Integer.parseInt(minWeight).toFloat(), Integer.parseInt(maxWeight).toFloat(), note, imgName)

            specieViewModel.insertSpecie(specie)

            Toast.makeText(requireContext(), "Specie Added.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkInput(speciesCode: String, fullName: String, authority: String): Boolean {
        return speciesCode.isNotEmpty() && fullName.isNotEmpty() && authority.isNotEmpty()
    }

}