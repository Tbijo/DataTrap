package com.example.datatrap.locality.presentation.locality_add_edit

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.datatrap.R
import com.example.datatrap.locality.data.LocalityEntity
import com.example.datatrap.locality.presentation.locality_list.LocalityListViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import java.util.Calendar

class LocalityViewModel: ViewModel() {

    private val localityListViewModel: LocalityListViewModel by viewModels()
    private lateinit var currentLocalityEntity: LocalityEntity

    private lateinit var requestMultiplePermissions: ActivityResultLauncher<Array<String>>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var cancellationTokenSource: CancellationTokenSource

    init {
        binding.btnGetCoorA.setOnClickListener {
            getCoordinates(true)
        }

        binding.btnGetCoorB.setOnClickListener {
            getCoordinates(false)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        cancellationTokenSource = CancellationTokenSource()

        requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
            perms.entries.forEach {
                Log.d("PERMISSIONS RESULT", "${it.key} = ${it.value}")
            }
        }
        // Destroy
        cancellationTokenSource.cancel()
        R.id.menu_save -> insertLocality()

        // UPDATE
        localityListViewModel.getLocality(args.locList.localityId).observe(viewLifecycleOwner) {
            initLocalityValuesToView(it)
            currentLocalityEntity = it
        }
        R.id.menu_save -> updateLocality()
        R.id.menu_delete -> deleteLocality()
    }

    private fun insertLocality() {
        val localityName = binding.etLocalityName.text.toString()
        val latitudeA = binding.etLatitudeA.text.toString().ifBlank { null }
        val longnitudeA = binding.etLongnitudeA.text.toString().ifBlank { null }
        val latitudeB = binding.etLatitudeB.text.toString().ifBlank { null }
        val longnitudeB = binding.etLongnitudeB.text.toString().ifBlank { null }

        if (checkInput(localityName)){
            val localityNote = if (binding.etLocalityNote.text.toString().isBlank()) null else binding.etLocalityNote.text.toString()

            val localityEntity = LocalityEntity(0, localityName, Calendar.getInstance().time,
                null, latitudeA?.toFloat(), longnitudeA?.toFloat(),
                latitudeB?.toFloat(), longnitudeB?.toFloat(),
                0, localityNote)

            localityListViewModel.insertLocality(localityEntity)

            Toast.makeText(requireContext(), "New locality added.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkInput(localityName: String): Boolean {
        return localityName.isNotEmpty()
    }

    ///////////////////////////////////GPS/////////////////////////////////////////////

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
                            binding.etLatitudeA.setText(it.latitude.toString())
                            binding.etLongnitudeA.setText(it.longitude.toString())
                        } else {
                            binding.etLatitudeB.setText(it.latitude.toString())
                            binding.etLongnitudeB.setText(it.longitude.toString())
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

    private fun initLocalityValuesToView(localityEntity: LocalityEntity){
        binding.etLocalityName.setText(localityEntity.localityName)
        binding.etLocalityNote.setText(localityEntity.note.toString())
        binding.etSessionNum.setText(localityEntity.numSessions.toString())

        binding.etUpLatA.setText(localityEntity.xA.toString())
        binding.etUpLongA.setText(localityEntity.yA.toString())
        binding.etUpLatB.setText(localityEntity.xB.toString())
        binding.etUpLongB.setText(localityEntity.yB.toString())
    }

    private fun deleteLocality() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            // odstranit lokalitu
            localityListViewModel.deleteLocality(args.locList.localityId)

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
            val localityEntity: LocalityEntity = currentLocalityEntity.copy()
            localityEntity.localityName = localityName
            localityEntity.numSessions = Integer.parseInt(binding.etSessionNum.text.toString())
            localityEntity.xA = latitudeA?.toFloat()
            localityEntity.yA = longitudeA?.toFloat()
            localityEntity.xB = latitudeB?.toFloat()
            localityEntity.yB = longitudeB?.toFloat()
            localityEntity.localityDateTimeUpdated = Calendar.getInstance().time
            localityEntity.note = if (binding.etLocalityNote.text.toString().isBlank() || binding.etLocalityNote.text.toString() == "null") null else binding.etLocalityNote.text.toString()

            localityListViewModel.updateLocality(localityEntity)

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