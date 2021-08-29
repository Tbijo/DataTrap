package com.example.datatrap.specie.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentUpdateSpecieBinding
import com.example.datatrap.models.Picture
import com.example.datatrap.models.Specie
import com.example.datatrap.viewmodels.PictureViewModel
import com.example.datatrap.viewmodels.SpecieViewModel

class UpdateSpecieFragment : Fragment() {

    private var _binding: FragmentUpdateSpecieBinding? = null
    private val binding get() = _binding!!
    private lateinit var specieViewModel: SpecieViewModel
    private lateinit var pictureViewModel: PictureViewModel
    private val args by navArgs<UpdateSpecieFragmentArgs>()

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateSpecieBinding.inflate(inflater, container, false)
        specieViewModel = ViewModelProvider(this).get(SpecieViewModel::class.java)
        pictureViewModel = ViewModelProvider(this).get(PictureViewModel::class.java)

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

        if (args.specie.imgName != null){
            binding.tvPicture.text = getString(R.string.pictureAdded)
        }

        /*Vybrat jednu fotku z databazy a zobrazit nazov*/
        binding.tvPicture.text = args.specie.imgName.toString()

        binding.btnPicture.setOnClickListener {
            updatePicture()
        }

        binding.btnAddSpecie.setOnClickListener {
            updateSpecie()
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete -> deleteSpecie()
        }
        return super.onOptionsItemSelected(item)
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

        var img = args.specie.imgName

        if (checkInput(speciesCode, fullName, authority)){

            if (imageUri != null){
                img = "specie_$speciesCode"
                val picture = Picture(img, imageUri.toString(), null)
                pictureViewModel.insertPicture(picture)
            }

            val specie = Specie(args.specie.specieId, speciesCode, fullName, synonym, authority, description, isSmallMammal, Integer.parseInt(upperFingers), Integer.parseInt(minWeight).toFloat(), Integer.parseInt(maxWeight).toFloat(), note, img)

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

    private fun updatePicture() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLauncher.launch(gallery)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imageUri = result.data?.data
            binding.tvPicture.text = getString(R.string.pictureAdded)
        }
    }

}