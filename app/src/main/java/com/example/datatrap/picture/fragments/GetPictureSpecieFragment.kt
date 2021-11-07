package com.example.datatrap.picture.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.example.datatrap.models.Picture
import com.example.datatrap.viewmodels.PictureViewModel
import com.example.datatrap.viewmodels.SharedViewModel
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class GetPictureSpecieFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentGetPictureSpecieBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var pictureViewModel: PictureViewModel
    private val args by navArgs<GetPictureSpecieFragmentArgs>()

    private var picture: Picture? = null
    private var oldPicName: String? = null
    private var picName: String? = null
    private var picUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentGetPictureSpecieBinding.inflate(inflater, container, false)
        pictureViewModel = ViewModelProvider(this).get(PictureViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        oldPicName = args.picName

        if (oldPicName != null){
            pictureViewModel.getPictureById(oldPicName!!)
        } else {
            binding.tvGetPicture.text = getString(R.string.noPicture)
        }
        pictureViewModel.gotPicture.observe(viewLifecycleOwner, {
            // ak mame fotku tak hu nacitame
            picture = it
            binding.ivGetPicture.setImageURI(it.path.toUri())
            picName = oldPicName
            binding.tvGetPicture.text = getString(R.string.pictureAdded)
        })

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
            // v pripade novej fotky treba staru fotku vymazat a ulozit novu fotku v databaze
            // poslat novy nazov a odist
            if (picName != oldPicName && picName != null) {
                val newPicture = Picture(picName!!, picUri.toString(), binding.etPicNote.text.toString())
                // vymazat povodnu fotku z databazy
                if (picture != null) {
                    pictureViewModel.deletePicture(picture!!)
                    // vymazat povodnu fotku ako subor
                    val myFile = File(picture!!.path)
                    if (myFile.exists()) myFile.delete()
                }
                // a novu ulozit
                pictureViewModel.insertPicture(newPicture)
                // poslat ID novej fotky
                sharedViewModel.setData(picName!!)
            } else {
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
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    private fun requestStoragePermission() {
        EasyPermissions.requestPermissions(
            this,
            "This app can not work without Storage Permission.", // tuto bude odkaz pre pouzivatela ak neda povolenie po
            1,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

}