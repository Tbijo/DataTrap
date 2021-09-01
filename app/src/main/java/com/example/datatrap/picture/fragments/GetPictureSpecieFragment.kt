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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentGetPictureSpecieBinding
import com.example.datatrap.models.Picture
import com.example.datatrap.viewmodels.PictureViewModel
import com.example.datatrap.viewmodels.SharedViewModel

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

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setData(imgName: String){
        sharedViewModel.setData(imgName)
    }

    private fun checkInput(){
        if (picUri != null){
            img = "specie_$speciesCode"
            val picture = Picture(img, picUri.toString(), null)
            pictureViewModel.insertPicture(picture)
        }
    }

    private fun getPicture(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLauncher.launch(gallery)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            picUri = result.data?.data
            binding.tvPicture.text = getString(R.string.pictureAdded)
        }
    }

}