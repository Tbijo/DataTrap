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
import com.example.datatrap.viewmodels.PictureViewModel
import com.example.datatrap.viewmodels.SharedViewModel
import com.example.datatrap.viewmodels.SpecieViewModel
import java.io.File
import java.util.*

class UpdateSpecieFragment : Fragment() {

    private var _binding: FragmentUpdateSpecieBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateSpecieFragmentArgs>()
    private lateinit var specieViewModel: SpecieViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var pictureViewModel: PictureViewModel

    private var imgName: String? = null
    private var upperFingers: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateSpecieBinding.inflate(inflater, container, false)
        specieViewModel = ViewModelProvider(this).get(SpecieViewModel::class.java)
        pictureViewModel = ViewModelProvider(this).get(PictureViewModel::class.java)

        imgName = args.specie.imgName

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.dataToShare.observe(requireActivity(), Observer {
            imgName = it
        })
        pictureViewModel.gotPicture.observe(viewLifecycleOwner, {
            // odstranit fyzicku zlozku
            val myFile = File(it.path)
            if (myFile.exists()) myFile.delete()
            //odstranit zaznam z databazy
            pictureViewModel.deletePicture(it)

            specieViewModel.deleteSpecie(args.specie)

            Toast.makeText(requireContext(),"Specie deleted.", Toast.LENGTH_LONG).show()

            findNavController().navigateUp()
        })

        binding.rgUpperFingers.setOnCheckedChangeListener { radioGroup, radioButtonId ->
            when (radioButtonId){
                binding.rb4.id -> upperFingers = 4
                binding.rb5.id -> upperFingers = 5
                binding.rbFingerNull.id -> upperFingers = null
            }
        }

        initSpecieValuesToView()

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

    private fun goToCamera(){
        val action = UpdateSpecieFragmentDirections.actionUpdateSpecieFragmentToGetPictureFragment(imgName)
        findNavController().navigate(action)
    }

    private fun initSpecieValuesToView(){
        binding.etSpeciesCode.setText(args.specie.speciesCode)
        binding.etFullName.setText(args.specie.fullName)
        binding.etAuthority.setText(args.specie.authority)
        binding.etDescription.setText(args.specie.description)
        binding.etSynonym.setText(args.specie.synonym)

        when(args.specie.upperFingers){
            4 -> binding.rb4.isChecked = true
            5 -> binding.rb5.isChecked = true
        }

        binding.etMaxWeight.setText(args.specie.maxWeight.toString())
        binding.etMinWeight.setText(args.specie.maxWeight.toString())
        binding.cbIsSmallMammal.isChecked = args.specie.isSmallMammal
        binding.etNote.setText(args.specie.note)
    }

    private fun deleteSpecie() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            if (imgName != null) {
                pictureViewModel.getPictureById(imgName!!)
            } else {
                specieViewModel.deleteSpecie(args.specie)

                Toast.makeText(requireContext(),"Specie deleted.", Toast.LENGTH_LONG).show()

                findNavController().navigateUp()
            }
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete Specie?")
            .setMessage("Are you sure you want to delete this specie?")
            .create().show()
    }

    private fun updateSpecie() {
        val speciesCode = binding.etSpeciesCode.text.toString()
        val fullName = binding.etFullName.text.toString()
        val authority = binding.etAuthority.text.toString()

        if (checkInput(speciesCode, fullName, authority)){

            val specie: Specie = args.specie.copy()
            specie.speciesCode = speciesCode
            specie.fullName = fullName
            specie.authority = authority
            specie.synonym = if (binding.etSynonym.text.toString().isBlank()) null else binding.etSynonym.text.toString()
            specie.description = if (binding.etDescription.text.toString().isBlank()) null else binding.etDescription.text.toString()
            specie.isSmallMammal = binding.cbIsSmallMammal.isChecked
            specie.upperFingers = upperFingers
            specie.minWeight = if (binding.etMinWeight.text.toString().isBlank()) null else binding.etMinWeight.text.toString().toFloat()
            specie.maxWeight = if (binding.etMaxWeight.text.toString().isBlank()) null else binding.etMaxWeight.text.toString().toFloat()
            specie.note = if (binding.etNote.text.toString().isBlank()) null else binding.etNote.text.toString()
            specie.imgName = imgName
            specie.specieDateTimeUpdated = Calendar.getInstance().time

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