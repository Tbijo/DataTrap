package com.example.datatrap.specie.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentUpdateSpecieBinding
import com.example.datatrap.models.Specie
import com.example.datatrap.viewmodels.SharedViewModel
import com.example.datatrap.viewmodels.SpecieViewModel

class UpdateSpecieFragment : Fragment() {

    private var _binding: FragmentUpdateSpecieBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateSpecieFragmentArgs>()
    private lateinit var specieViewModel: SpecieViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private var imgName: String? = args.specie.imgName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateSpecieBinding.inflate(inflater, container, false)
        specieViewModel = ViewModelProvider(this).get(SpecieViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.dataToShare.observe(requireActivity(), Observer {
            imgName = it
        })

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

    private fun goToCamera(){
        // overenie ci mame alebo nemame fotku
        val action = UpdateSpecieFragmentDirections.actionUpdateSpecieFragmentToGetPictureFragment(imgName)
        findNavController().navigate(action)
    }

    private fun deleteSpecie() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            specieViewModel.deleteSpecie(args.specie)

            Toast.makeText(requireContext(),"Specie deleted.", Toast.LENGTH_LONG).show()

            findNavController().navigateUp()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete Specie?")
            .setMessage("Are you sure you want to delete this specie?")
            .create().show()
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

        if (checkInput(speciesCode, fullName, authority)){

            val specie = Specie(args.specie.specieId, speciesCode, fullName, synonym, authority, description, isSmallMammal, Integer.parseInt(upperFingers), Integer.parseInt(minWeight).toFloat(), Integer.parseInt(maxWeight).toFloat(), note, imgName)

            specieViewModel.updateSpecie(specie)

            Toast.makeText(requireContext(), "Specie Updated.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
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