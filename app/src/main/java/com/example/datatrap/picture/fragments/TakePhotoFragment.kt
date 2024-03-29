package com.example.datatrap.picture.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentTakePhotoBinding
import com.example.datatrap.models.MouseImage
import com.example.datatrap.models.OccasionImage
import com.example.datatrap.viewmodels.MouseImageViewModel
import com.example.datatrap.viewmodels.OccasionImageViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class TakePhotoFragment : Fragment() {

    private var _binding: FragmentTakePhotoBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<TakePhotoFragmentArgs>()
    private val occasion = "Occasion"
    private val mouse = "Mouse"
    private val mouseImageViewModel: MouseImageViewModel by viewModels()
    private val occasionImageViewModel: OccasionImageViewModel by viewModels()
    private var mouseImage: MouseImage? = null
    private var occasionImage: OccasionImage? = null

    private var imagePathUri: Uri? = null
    private var imageName: String? = null
    private lateinit var deviceID: String

    private lateinit var requestMultiplePermissions: ActivityResultLauncher<Array<String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTakePhotoBinding.inflate(inflater, container, false)

        deviceID =
            Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)

        setImageIfExists()

        binding.btnTakePicture.setOnClickListener {
            if (hasPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                when (args.fragmentName) {
                    occasion -> takeImage(occasion)
                    mouse -> takeImage(mouse)
                }
            } else {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            }
        }

        binding.btnAddPicture.setOnClickListener {
            // ak je vsetko v poriadku treba
            // v pripade novej fotky treba staru fotku vymazat a ulozit novu fotku v databaze aj subor
            // pridat novu fotku do galerie

            if (imageName == null && mouseImage == null && occasionImage == null) {
                Toast.makeText(requireContext(), "No image was found.", Toast.LENGTH_LONG).show()
            } else {
                saveAndAddImage()
            }
        }

        requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
            perms.entries.forEach {
                Log.d("PERMISSIONS RESULT", "${it.key} = ${it.value}")
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setImageIfExists() {
        when (args.fragmentName) {
            mouse -> {
                mouseImageViewModel.getImageForMouse(args.parentId, args.deviceID).observe(viewLifecycleOwner) {
                    // ci mame fotku
                    if (it != null) {
                        mouseImage = it
                        binding.tvTakePicture.text = getString(R.string.pictureAdded)
                        binding.ivTakePicture.setImageURI(it.path.toUri())
                        binding.etPicNote.setText(it.note)
                    } else {
                        binding.tvTakePicture.text = getString(R.string.noPicture)
                    }
                }
            }

            occasion -> {
                occasionImageViewModel.getImageForOccasion(args.parentId)
                    .observe(viewLifecycleOwner) {
                        // ci mame fotku
                        if (it != null) {
                            occasionImage = it
                            binding.tvTakePicture.text = getString(R.string.pictureAdded)
                            binding.ivTakePicture.setImageURI(it.path.toUri())
                            binding.etPicNote.setText(it.note)
                        } else {
                            binding.tvTakePicture.text = getString(R.string.noPicture)
                        }
                    }
            }
        }
    }

    private fun takeImage(what: String) {
        Log.d("takeImage", "Ideme robit fotku")
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // odstranit predch. subor ak existuje
            if (imagePathUri != null) {
                val myFile = File(imagePathUri.toString())
                if (myFile.exists()) myFile.delete()
            }
            // potom vytvorit novy
            takePictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                val photoFile: File? = try {
                    createImageFile(what)
                } catch (ex: IOException) {
                    Toast.makeText(requireContext(), "File was not created.", Toast.LENGTH_LONG)
                        .show()
                    null
                }
                photoFile?.also {
                    imagePathUri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.datatrap.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imagePathUri)
                    resultLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(what: String): File {
        Log.d("createImageFile", "Vyrabame fotku")
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance()
        val dateTime = formatter.format(date)
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "${what}_${dateTime}_",
            ".jpg",
            storageDir
        ).apply {
            imageName = nameWithoutExtension
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("resultLauncher", "Fotka sa vratila")
            if (result.resultCode != Activity.RESULT_OK) {
                Log.d("resultLauncher", "Fotka Nepresla")
                // sem sa pojde ak pouzivatel neprijal fotku
                // treba vymazat empty file ktora bola vytvorena
                val myFile = File(imagePathUri.toString())
                if (myFile.exists()) {
                    myFile.delete()
                    Toast.makeText(requireContext(), "Empty file deleted.", Toast.LENGTH_SHORT)
                        .show()
                }
                binding.tvTakePicture.text = getString(R.string.noPicture)
                imagePathUri = null
                imageName = null
                Toast.makeText(requireContext(), "Parameters cleared.", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("resultLauncher", "Fotka Presla")
                binding.ivTakePicture.setImageURI(imagePathUri)
                binding.tvTakePicture.text = getString(R.string.pictureAdded)
            }
        }

    private fun galleryAddImage(imageUri: Uri) {
        val bitmap = BitmapFactory.decodeFile(imageUri.toString())

        MediaStore.Images.Media.insertImage(
            requireContext().contentResolver,
            bitmap,
            imageName,
            "Image of $imageName"
        )
        Toast.makeText(requireContext(), "Image Added to Gallery", Toast.LENGTH_SHORT).show()
    }

    private fun saveAndAddImage() {
        val note = binding.etPicNote.text.toString()
        when (args.fragmentName) {
            mouse -> {
                // vytvara sa nova fotka pre mys, stara nebola
                if (mouseImage == null) {
                    mouseImage = MouseImage(
                        0,
                        imageName!!,
                        imagePathUri.toString(),
                        note,
                        args.parentId,
                        deviceID,
                        Calendar.getInstance().time.time
                    )
                    mouseImageViewModel.insertImage(mouseImage!!)
                    galleryAddImage(imagePathUri!!)

                    // existuje stara fotka
                } else {
                    // ostava stara fotka
                    if (imageName == null) {
                        mouseImage!!.note = note
                        mouseImageViewModel.insertImage(mouseImage!!)

                        // nahradza sa stara fotka novou fotkou
                    } else {

                        // vymazat zaznam fotky z databazy
                        // vymazat fyzicky subor fotky
                        mouseImageViewModel.deleteImage(mouseImage!!.mouseImgId, mouseImage!!.path)
                        // pre istotu
                        mouseImage = null
                        // pridat zaznam novej fotky do databazy subor uz existuje
                        mouseImage = MouseImage(
                            0,
                            imageName!!,
                            imagePathUri.toString(),
                            note,
                            args.parentId,
                            deviceID,
                            Calendar.getInstance().time.time
                        )
                        mouseImageViewModel.insertImage(mouseImage!!)
                        // pridat novu fotku do galerie
                        galleryAddImage(imagePathUri!!)
                    }
                }
            }

            occasion -> {
                // vytvara sa nova fotka pre akciu, stara nebola
                if (occasionImage == null) {
                    occasionImage = OccasionImage(
                        0,
                        imageName!!,
                        imagePathUri.toString(),
                        note,
                        args.parentId,
                        Calendar.getInstance().time.time
                    )
                    occasionImageViewModel.insertImage(occasionImage!!)
                    galleryAddImage(imagePathUri!!)

                    // existuje stara fotka
                } else {
                    // ostava stara fotka
                    if (imageName == null) {
                        occasionImage!!.note = note
                        occasionImageViewModel.insertImage(occasionImage!!)

                        // nahradza sa stara fotka novou fotkou
                    } else {

                        // vymazat zaznam fotky z databazy
                        // vymazat fyzicky subor fotky
                        occasionImageViewModel.deleteImage(
                            occasionImage!!.occasionImgId,
                            occasionImage!!.path
                        )
                        // pre istotu
                        occasionImage = null
                        // pridat zaznam novej fotky do databazy subor uz existuje
                        occasionImage = OccasionImage(
                            0,
                            imageName!!,
                            imagePathUri.toString(),
                            note,
                            args.parentId,
                            Calendar.getInstance().time.time
                        )
                        occasionImageViewModel.insertImage(occasionImage!!)
                        // pridat novu fotku do galerie
                        galleryAddImage(imagePathUri!!)
                    }
                }
            }
        }

        findNavController().navigateUp()

    }

    ////////////////////////////////////PERMISSIONS///////////////////////////////////////////

    private fun hasPermissions(vararg permissions: String): Boolean {
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    it
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private fun requestPermissions(permissions: Array<String>) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                permissions[0]
            )
        ) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Application needs this permission to work.")
                .setPositiveButton("OK") { _, _ ->
                    requestAppSettings()
                }
                .setNegativeButton("Cancel") { _, _ ->
                    Toast.makeText(
                        requireContext(),
                        "Application can not proceed.",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                .create().show()
            Log.d("PERMISSIONS", "DOUBLE DENIAL")

        } else {
            Log.d("PERMISSIONS", "GETTING PERMISSIONS")
            requestMultiplePermissions.launch(
                permissions
            )
        }
    }

    private fun requestAppSettings(){
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}