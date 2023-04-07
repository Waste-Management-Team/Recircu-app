package com.godzuche.recircu.core.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log
import com.godzuche.recircu.core.domain.location.LocationClient
import com.godzuche.recircu.core.util.hasLocationPermission
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultLocationClient @Inject constructor(
    private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationClient {
    @SuppressLint("MissingPermission")
    override fun getLocation(): Flow<LocationResult> {
        return callbackFlow {
            if (!context.hasLocationPermission()) {
                launch {
                    send(LocationResult.Error(errorType = LocationErrorType.MISSING_PERMISSION))
                    Log.d("Location", "getLocation() called err perms")
                }
//                throw LocationClient.LocationException("Missing location permission")
            }
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGpsEnabled && !isNetworkEnabled) {
                launch {
                    send(LocationResult.Error(errorType = LocationErrorType.DISABLED_GPS))
                    Log.d("Location", "getLocation() called err gps")
                }
//                throw LocationClient.LocationException("GPS is disabled")
            }

            val lastLocationResult = client.lastLocation
            lastLocationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    launch {
                        Log.d("Location", "getLocation() called success")
                        task.result?.let {
                            send(LocationResult.Success(it))
                        }
                    }
                }
            }
            awaitClose {
                // Not needed
            }
        }
    }
}

sealed interface LocationResult {
    data class Error(val errorType: LocationErrorType) : LocationResult
    data class Success(val location: Location) : LocationResult
}

enum class LocationErrorType {
    MISSING_PERMISSION,
    DISABLED_GPS
}