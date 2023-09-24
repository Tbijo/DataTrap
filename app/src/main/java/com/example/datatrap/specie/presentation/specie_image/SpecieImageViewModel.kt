package com.example.datatrap.specie.presentation.specie_image

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.R
import com.example.datatrap.specie.data.specie_image.SpecieImageEntity
import com.example.datatrap.specie.data.specie_image.SpecieImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class SpecieImageViewModel @Inject constructor(
    private val specieImageRepository: SpecieImageRepository
) : ViewModel() {

    private val specieImageViewModel: SpecieImageViewModel by viewModels()
    private var imageName: String? = null
    private var imageUri: Uri? = null
    private var specieImageEntity: SpecieImageEntity? = null
    private lateinit var deviceID: String

    private lateinit var requestMultiplePermissions: ActivityResultLauncher<Array<String>>

    init {
        deviceID =
            Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)

        setImageIfExists()

        binding.btnGetPicture.setOnClickListener {
            if (hasPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                // zohnat novu fotku
                getPicture()
            } else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }
        }

        binding.btnAddPicture.setOnClickListener {
            // ak je vsetko v poriadku treba
            // v pripade novej fotky treba staru fotku vymazat a ulozit novu fotku v databaze aj fyzicky
            if (imageName == null && specieImageEntity == null) {
                Toast.makeText(requireContext(), "No image was found.", Toast.LENGTH_LONG).show()
            } else {
                saveImage()
            }
        }

        requestMultiplePermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
                perms.entries.forEach {
                    Log.d("PERMISSIONS RESULT", "${it.key} = ${it.value}")
                }
            }
    }

    private fun saveImage() {
        val note = binding.etPicNote.text.toString()

        // vytvara sa nova fotka, stara nebola
        if (specieImageEntity == null) {
            specieImageEntity =
                SpecieImageEntity(0, imageName!!, imageUri.toString(), note, args.parentId, Calendar.getInstance().time.time)
            specieImageViewModel.insertImage(specieImageEntity!!)

            // stara fotka existuje
        } else {

            // ostava stara fotka
            if (imageName == null) {
                specieImageEntity!!.note = note
                specieImageViewModel.insertImage(specieImageEntity!!)

                // nahradi sa stara fotka novou fotkou
            } else {

                // vymazat zaznam starej fotky v databaze
                specieImageViewModel.deleteImage(specieImageEntity!!.specieImgId, specieImageEntity!!.path)
                // pre istotu
                specieImageEntity = null
                // pridat zaznam novej fotky do databazy subor uz existuje
                specieImageEntity =
                    SpecieImageEntity(0, imageName!!, imageUri.toString(), note, args.parentId, Calendar.getInstance().time.time)
                specieImageViewModel.insertImage(specieImageEntity!!)
            }
        }

        findNavController().navigateUp()
    }

    private fun setImageIfExists() {
        specieImageViewModel.getImageForSpecie(args.parentId).observe(viewLifecycleOwner) {
            // ak mame fotku tak hu nacitame
            if (it != null) {
                specieImageEntity = it
                binding.ivGetPicture.setImageURI(it.path.toUri())
                binding.tvGetPicture.text = getString(R.string.pictureAdded)
                binding.etPicNote.setText(it.note)
            } else {
                binding.tvGetPicture.text = getString(R.string.noPicture)
            }
        }
    }

    private fun getPicture() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLauncher.launch(gallery)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // pouzivatel akceptoval fotku
                imageUri = result.data?.data
                val date = Calendar.getInstance().time
                val formatter = SimpleDateFormat.getDateTimeInstance()
                val dateTime = formatter.format(date)
                imageName = "Specie_$dateTime"
                binding.ivGetPicture.setImageURI(imageUri)
                binding.tvGetPicture.text = getString(R.string.pictureAdded)
            }
        }

    ////////////////////PERMISSIONS//////////////////////////////////////////////

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

    private fun requestAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun insertImage(specieImageEntity: SpecieImageEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            specieImageRepository.insertImage(specieImageEntity)
        }
    }

    fun deleteImage(specieImgId: Long, imagePath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val job1 = launch {
                // vymazat subor starej fotky
                val myFile = File(imagePath)
                if (myFile.exists()) myFile.delete()
            }
            val job2 = launch {
                specieImageRepository.deleteImage(specieImgId)
            }
            job1.join()
            job2.join()
        }
    }

    fun getImageForSpecie(specieId: Long): LiveData<SpecieImageEntity> {
        return specieImageRepository.getImageForSpecie(specieId)
    }
}