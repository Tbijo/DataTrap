package com.example.datatrap.locality.fragments.gps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class GPSProvider(
    private val context: Context,
    private val activity: FragmentActivity,
    private val fragment: Fragment
) : EasyPermissions.PermissionCallbacks {

    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(context, "Permission Granted.", Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(fragment, perms)) {
            AppSettingsDialog.Builder(activity).build().show()
        } else {
            requestLocationPermission()
        }
    }

    private fun hasLocationPermission() =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            fragment,
            "This app can not work without Location Permission.",
            1,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun isGPSon(context: Context): Boolean {
        val locationManager =
            context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    fun getCoordinates(listener: CoordinatesListener) {
        if (isGPSon(context)) {
            if (hasLocationPermission()) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        listener.onReceivedCoordinates(it.latitude, it.longitude)
                    }
                }
            } else {
                requestLocationPermission()
            }
        } else {
            Toast.makeText(context, "Turn on GPS.", Toast.LENGTH_LONG).show()
        }
    }

    interface CoordinatesListener {
        fun onReceivedCoordinates(latitude: Double, longitude: Double)
    }
}