package com.godzuche.recircu.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.godzuche.recircu.core.util.hasLocationPermission
import com.godzuche.recircu.domain.location.LocationClient
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class DefaultLocationClient @Inject constructor(
    private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationClient {
    @SuppressLint("MissingPermission")
    override fun getLocation(): Flow<LocationResult> {
//        val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        return flow {
            val a = coroutineContext
            if (!context.hasLocationPermission()) {
                LocationResult.Error(errorType = LocationErrorType.MISSING_PERMISSION)
//                throw LocationClient.LocationException("Missing location permission")
            }
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGpsEnabled && !isNetworkEnabled) {
                LocationResult.Error(errorType = LocationErrorType.DISABLED_GPS)
//                throw LocationClient.LocationException("GPS is disabled")
            }

            val lastLocationResult = client.lastLocation
            lastLocationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    CoroutineScope(a).launch(Dispatchers.IO) {
                        emit(LocationResult.Success(task.result))
                    }
                }
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