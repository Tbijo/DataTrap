package com.example.datatrap.locality.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentUpdateLocalityBinding
import com.example.datatrap.models.Locality
import com.example.datatrap.viewmodels.LocalityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class UpdateLocalityFragment : Fragment(){

    private var _binding: FragmentUpdateLocalityBinding? = null
    private val binding get() = _binding!!
    
    private val localityViewModel: LocalityViewModel by viewModels()
    private val args by navArgs<UpdateLocalityFragmentArgs>()
    private lateinit var currentLocality: Locality

    private lateinit var requestMultiplePermissions: ActivityResultLauncher<Array<String>>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var cancellationTokenSource: CancellationTokenSource

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentUpdateLocalityBinding.inflate(inflater, container, false)

        localityViewModel.getLocality(args.locList.localityId).observe(viewLifecycleOwner) {
            initLocalityValuesToView(it)
            currentLocality = it
        }

        binding.btnGetCoorA.setOnClickListener {
            getCoordinates(true)
        }

        binding.btnUpGetCoorB.setOnClickListener {
            getCoordinates(false)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        cancellationTokenSource = CancellationTokenSource()

        requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
                perms.entries.forEach {
                    Log.d("PERMISSIONS RESULT", "${it.key} = ${it.value}")
                }
            }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        cancellationTokenSource.cancel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> updateLocality()
            R.id.menu_delete -> deleteLocality()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initLocalityValuesToView(locality: Locality){
        binding.etLocalityName.setText(locality.localityName)
        binding.etLocalityNote.setText(locality.note.toString())
        binding.etSessionNum.setText(locality.numSessions.toString())

        binding.etUpLatA.setText(locality.xA.toString())
        binding.etUpLongA.setText(locality.yA.toString())
        binding.etUpLatB.setText(locality.xB.toString())
        binding.etUpLongB.setText(locality.yB.toString())
    }

    private fun deleteLocality() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            // odstranit lokalitu
            localityViewModel.deleteLocality(args.locList.localityId)

            Toast.makeText(requireContext(),"Locality deleted.", Toast.LENGTH_LONG).show()

            findNavController().navigateUp()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete Locality?")
            .setMessage("Are you sure you want to delete this locality?")
            .create().show()
    }

    private fun updateLocality() {
        val localityName = binding.etLocalityName.text.toString()
        val latitudeA = binding.etUpLatA.text.toString().ifBlank { null }
        val longitudeA = binding.etUpLongA.text.toString().ifBlank { null }
        val latitudeB = binding.etUpLatB.text.toString().ifBlank { null }
        val longitudeB = binding.etUpLongB.text.toString().ifBlank { null }

        if (checkInput(localityName)){
            val locality: Locality = currentLocality.copy()
            locality.localityName = localityName
            locality.numSessions = Integer.parseInt(binding.etSessionNum.text.toString())
            locality.xA = latitudeA?.toFloat()
            locality.yA = longitudeA?.toFloat()
            locality.xB = latitudeB?.toFloat()
            locality.yB = longitudeB?.toFloat()
            locality.localityDateTimeUpdated = Calendar.getInstance().time
            locality.note = if (binding.etLocalityNote.text.toString().isBlank() || binding.etLocalityNote.text.toString() == "null") null else binding.etLocalityNote.text.toString()

            localityViewModel.updateLocality(locality)

            Toast.makeText(requireContext(), "Locality updated.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkInput(localityName: String): Boolean {
        return localityName.isNotEmpty()
    }

    ////////////////////////////////////GPS///////////////////////////////////////////

    private fun isGPSon(context: Context): Boolean {
        val locationManager =
            context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    fun getCoordinates(isA: Boolean) {
        if (isGPSon(requireContext())) {
            if (hasPermissions(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d("GPS", "GETTING COORDINATES")
                val currentLocationTask: Task<Location> = fusedLocationClient.getCurrentLocation(
                    LocationRequest.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                )
                currentLocationTask.addOnSuccessListener { location ->
                    location?.let {
                        Log.d("GPS", "LOCATION IS NOT NULL")
                        if (isA) {
                            binding.etUpLatA.setText(it.latitude.toString())
                            binding.etUpLongA.setText(it.longitude.toString())
                        } else {
                            binding.etUpLatB.setText(it.latitude.toString())
                            binding.etUpLongB.setText(it.longitude.toString())
                        }
                    }
                }
            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
            }
        } else {
            Toast.makeText(requireContext(), "Turn on GPS.", Toast.LENGTH_LONG).show()
        }
    }

    //////////////////////////PERMISSIONS//////////////////////////////////////////////////

    private fun hasPermissions(vararg permissions: String): Boolean {
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED) {
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
                    Toast.makeText(requireContext(), "Application can not proceed.", Toast.LENGTH_LONG)
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