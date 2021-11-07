package com.example.datatrap.picture.fragments

import android.Manifest
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
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentTakePhotoBinding
import com.example.datatrap.models.Picture
import com.example.datatrap.viewmodels.PictureViewModel
import com.example.datatrap.viewmodels.SharedViewModel
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class TakePhotoFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentTakePhotoBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var pictureViewModel: PictureViewModel
    private val args by navArgs<TakePhotoFragmentArgs>()

    private var picture: Picture? = null
    private var oldPicName: String? = null
    private var picPath: String? = null
    private var picUri: Uri? = null
    private var picName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTakePhotoBinding.inflate(inflater, container, false)
        pictureViewModel = ViewModelProvider(this).get(PictureViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        oldPicName = args.picName

        if (oldPicName != null) {
            pictureViewModel.getPictureById(oldPicName!!).observe(viewLifecycleOwner, {
                // ak mame fotku tak ju nacitame
                picture = it
                binding.tvTakePicture.text = getString(R.string.pictureAdded)
                binding.ivTakePicture.setImageURI(it.path.toUri())
                picName = oldPicName
            })
        } else {
            binding.tvTakePicture.text = getString(R.string.noPicture)
        }

        binding.btnTakePicture.setOnClickListener {
            if (hasStoragePermission()) {
                when (args.fragmentName) {
                    "Occasion" -> takePicture("Occasion")
                    "Mouse" -> takePicture("Mouse")
                }
            } else {
                requestStoragePermission()
            }
        }

        binding.btnAddPicture.setOnClickListener {
            // ak je vsetko v poriadku treba
            // v pripade novej fotky treba staru fotku vymazat a ulozit novu fotku v databaze
            // pridat novu fotku do galerie
            // poslat novy nazov a odist
            if (oldPicName != picName && picName != null) {
                val newPicture =
                    Picture(picName!!, picUri.toString(), binding.etPicNote.text.toString())
                // vymazat povodnu fotku z databazy
                if (picture != null) {
                    pictureViewModel.deletePicture(picture!!)
                    // vymazat povodnu fotku ako subor
                    val myFile = File(picture!!.path)
                    if (myFile.exists()) myFile.delete()
                }
                // ulozit novu
                pictureViewModel.insertPicture(newPicture)
                // zobrazit v galerii
                galleryAddPic(picUri!!, picName!!)
                // poslat ID novej fotky
                sharedViewModel.setData(picName!!)
            } else {
                // v pripade povodnej fotky len poslat povodny nazov a odist
                sharedViewModel.setData(picName!!)
            }

            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun takePicture(what: String) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                val photoFile: File? = try {
                    createImageFile(what)
                } catch (ex: IOException) {
                    Toast.makeText(requireContext(), "File was not created.", Toast.LENGTH_LONG)
                        .show()
                    null
                }
                photoFile?.also {
                    picUri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.datatrap.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri)
                    resultLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(what: String): File {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance()
        val dateTime = formatter.format(date)
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "${what}_${dateTime}_",
            ".jpg",
            storageDir
        ).apply {
            picPath = absolutePath
            picName = nameWithoutExtension
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) {
                // sem sa pojde ak pouzivatel neprijal fotku
                // treba vymazat empty file ktora bola vytvorena
                val myFile = File(picPath)
                if (myFile.exists()) myFile.delete()
                binding.tvTakePicture.text = getString(R.string.noPicture)
                picUri = null
                picPath = null
                picName = null
                Toast.makeText(requireContext(), "Empty File deleted.", Toast.LENGTH_SHORT).show()
            } else {
                binding.ivTakePicture.setImageURI(picUri)
                binding.tvTakePicture.text = getString(R.string.pictureAdded)
            }
        }

    private fun galleryAddPic(imageUri: Uri, title: String) {
        val bitmap =
            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
        MediaStore.Images.Media.insertImage(
            requireContext().contentResolver,
            bitmap,
            title,
            "Image of $title"
        )
        Toast.makeText(requireContext(), "Picture Added to Gallery", Toast.LENGTH_SHORT).show()
    }

    ///////
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(context, "Permission Granted.", Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestStoragePermission()
        }
    }

    private fun hasStoragePermission() =
        EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE    // sem pojdu permissiony ktore chceme skontrolovat ci su povolene
        )

    private fun requestStoragePermission() {
        EasyPermissions.requestPermissions(
            this,
            "This app can not work without Storage Permission.", // tuto bude odkaz pre pouzivatela ak neda povolenie po
            1,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE    // co chceme povolit
        )
    }

}