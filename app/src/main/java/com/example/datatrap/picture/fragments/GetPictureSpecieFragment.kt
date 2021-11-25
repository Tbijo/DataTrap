package com.example.datatrap.picture.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentGetPictureSpecieBinding
import com.example.datatrap.models.SpecieImage
import com.example.datatrap.viewmodels.SpecieImageViewModel
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class GetPictureSpecieFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentGetPictureSpecieBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<GetPictureSpecieFragmentArgs>()
    private lateinit var specieImageViewModel: SpecieImageViewModel

    private var imageName: String? = null
    private var imageUri: Uri? = null
    private var specieImage: SpecieImage? = null
    private lateinit var deviceID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentGetPictureSpecieBinding.inflate(inflater, container, false)
        specieImageViewModel = ViewModelProvider(this).get(SpecieImageViewModel::class.java)

        deviceID = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)

        setImageIfExists()

        binding.btnGetPicture.setOnClickListener {
            if (hasStoragePermission()) {
                // zohnat novu fotku
                getPicture()
            } else {
                requestStoragePermission()
            }
        }

        binding.btnAddPicture.setOnClickListener {
            // ak je vsetko v poriadku treba
            // v pripade novej fotky treba staru fotku vymazat a ulozit novu fotku v databaze aj fyzicky
            if (imageName == null && specieImage == null) {
                Toast.makeText(requireContext(), "No image was found.", Toast.LENGTH_LONG).show()
            } else {
                saveImage()
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun saveImage() {
        val note = binding.etPicNote.text.toString()

        // vytvara sa nova fotka, stara nebola
        if (specieImage == null) {
            specieImage = SpecieImage(0, imageName!!, imageUri.toString(), note, args.parentId, deviceID)
            specieImageViewModel.insertImage(specieImage!!)

        // stara fotka existuje
        } else {

            // ostava stara fotka
            if (imageName == null) {
                specieImage!!.note = note
                specieImageViewModel.insertImage(specieImage!!)

            // nahradi sa stara fotka novou fotkou
            } else {
                // vymazat subor starej fotky
                val myFile = File(specieImage!!.path)
                if (myFile.exists()) myFile.delete()
                // vymazat zaznam starej fotky v databaze
                specieImageViewModel.deleteImage(specieImage!!.specieImgId)
                // pre istotu
                specieImage = null
                // pridat zaznam novej fotky do databazy subor uz existuje
                specieImage = SpecieImage(0, imageName!!, imageUri.toString(), note, args.parentId, deviceID)
                specieImageViewModel.insertImage(specieImage!!)
            }
        }

        findNavController().navigateUp()
    }

    private fun setImageIfExists() {
        specieImageViewModel.getImageForSpecie(args.parentId).observe(viewLifecycleOwner, {
            // ak mame fotku tak hu nacitame
            if (it != null) {
                specieImage = it
                binding.ivGetPicture.setImageURI(it.path.toUri())
                binding.tvGetPicture.text = getString(R.string.pictureAdded)
                binding.etPicNote.setText(it.note)
            } else {
                binding.tvGetPicture.text = getString(R.string.noPicture)
            }
        })
    }

    private fun getPicture(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLauncher.launch(gallery)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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

    ////////////////////

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
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    private fun requestStoragePermission() {
        EasyPermissions.requestPermissions(
            this,
            "This app can not work without Storage Permission.", // tuto bude odkaz pre pouzivatela ak neda povolenie po
            1,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

}