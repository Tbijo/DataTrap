package com.example.datatrap.picture.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentGetPictureSpecieBinding
import com.example.datatrap.models.Picture
import com.example.datatrap.viewmodels.PictureViewModel
import com.example.datatrap.viewmodels.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class GetPictureSpecieFragment : Fragment() {

    private var _binding: FragmentGetPictureSpecieBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var pictureViewModel: PictureViewModel
    private val args by navArgs<GetPictureSpecieFragmentArgs>()

    private var picName: String? = null
    private var picUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentGetPictureSpecieBinding.inflate(inflater, container, false)
        pictureViewModel = ViewModelProvider(this).get(PictureViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        if (args.picName != null){
            // ak mame fotku tak hu nacitame
            binding.tvGetPicture.text = getString(R.string.pictureAdded)
            val picture: Picture? = pictureViewModel.getPictureById(args.picName!!)
            binding.ivGetPicture.setImageURI(picture?.path?.toUri())
            picName = args.picName
        }else{
            binding.tvGetPicture.text = getString(R.string.noPicture)
        }

        binding.btnGetPicture.setOnClickListener {
            // zohnat novu fotku
            getPicture()
        }

        binding.btnAddPicture.setOnClickListener {
            // ak je vsetko v poriadku treba
            // v pripade novej fotky treba staru fotku vymazat a ulozit novu fotku v databaze
            // poslat novy nazov a odist
            if (picName != args.picName && picName != null){
                val picture = Picture(picName!!, picUri.toString(), binding.etPicNote.text.toString())
                pictureViewModel.insertPicture(picture)
                sharedViewModel.setData(picName!!)
            }else{
                // v pripade povodnej fotky len poslat povodny nazov a odist
                sharedViewModel.setData(args.picName!!)
            }

            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getPicture(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLauncher.launch(gallery)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // pouzivatel akceptoval fotku
            picUri = result.data?.data
            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat.getDateTimeInstance()
            val dateTime = formatter.format(date)
            picName = "Specie_$dateTime"
            binding.ivGetPicture.setImageURI(picUri)
            binding.tvGetPicture.text = getString(R.string.pictureAdded)
        }
    }

}