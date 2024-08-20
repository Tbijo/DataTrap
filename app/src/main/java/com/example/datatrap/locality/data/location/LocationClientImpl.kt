package com.example.datatrap.locality.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.example.datatrap.core.presentation.permission.hasFineLocationPermission
import com.example.datatrap.locality.domain.GPSException
import com.example.datatrap.locality.domain.LocationClient
import com.example.datatrap.locality.domain.LocationException
import com.example.datatrap.locality.domain.MissingPermissionException
import com.example.datatrap.sync.utils.Result
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class LocationClientImpl(
    private val context: Context,
    private val client: FusedLocationProviderClient,
): LocationClient {

    private val cancellationTokenSource: CancellationTokenSource = CancellationTokenSource()

    override fun cancelLocationProvider() {
        // Destroy
        cancellationTokenSource.cancel()
    }

    @SuppressLint("MissingPermission")
    override fun getLocation(): Flow<Result<Location, LocationException>> {

        return callbackFlow {
            // check for locations permissions
            if(!context.hasFineLocationPermission()) {
                // permissions arent granted
                send(
                    Result.Error(MissingPermissionException())
                )
            }

            // check for location hardware accessibility
            // that means GPS or NetworkProvider enabled
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if(!isGpsEnabled && !isNetworkEnabled) {
                // no location hardware is enabled
                send(
                    Result.Error(GPSException())
                )
            }

            val currentLocationTask: Task<Location> = client.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            )
            currentLocationTask.addOnSuccessListener { location ->
                launch {
                    send(
                        Result.Success(location)
                    )
                }
            }
        }
    }

}