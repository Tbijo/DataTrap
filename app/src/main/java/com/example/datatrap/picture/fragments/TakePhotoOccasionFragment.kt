package com.example.datatrap.picture.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentTakePhotoOccasionBinding
import com.example.datatrap.models.Picture
import com.example.datatrap.viewmodels.PictureViewModel
import com.example.datatrap.viewmodels.SharedViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class TakePhotoOccasionFragment : Fragment() {

    private var _binding: FragmentTakePhotoOccasionBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var pictureViewModel: PictureViewModel

    private var myPath: String? = null
    private  var photoURI: Uri? = null
    private var title: String? = null
    private var imgName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentTakePhotoOccasionBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        pictureViewModel = ViewModelProvider(this).get(PictureViewModel::class.java)

        binding.btnOccPhoto.setOnClickListener {
            takePicture()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setData(imgName: String){
        sharedViewModel.setData(imgName)
        findNavController().navigateUp()
    }

    private fun checkInsert(){
        if (title != null){
            val picture = Picture(title!!, photoURI.toString(), binding.etOccPicNote.text.toString())
            pictureViewModel.insertPicture(picture)
            galleryAddPic(photoURI!!, title!!)
        }
    }

    private fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    Toast.makeText(requireContext(), "File was not created.", Toast.LENGTH_LONG).show()
                    null
                }
                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.datatrap.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    resultLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance()
        val formatedDate = formatter.format(date)
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "Occasion_${formatedDate}_",
            ".jpg",
            storageDir
        ).apply {
            myPath = absolutePath
            binding.tvOccPhoto.text = "Specie_$formatedDate"
            title = "Occasion_$formatedDate"
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            // sem sa pojde ak pouzivatel nespravil fotku
            // treba vymazat empty file ktora bola vytvorena
            val myFile: File = File(myPath)
            if (myFile.exists()) myFile.delete()
            binding.tvOccPhoto.text = getString(R.string.noPicture)
            title = null
            photoURI = null
            myPath = null
            Toast.makeText(requireContext(), "Empty File deleted.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun galleryAddPic(imageUri:Uri, title:String) {
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri)
        MediaStore.Images.Media.insertImage(
            requireContext().contentResolver,
            bitmap,
            title,
            "Image of $title"
        )
        Toast.makeText(requireContext(), "Picture Added to Gallery", Toast.LENGTH_SHORT).show()
    }

}