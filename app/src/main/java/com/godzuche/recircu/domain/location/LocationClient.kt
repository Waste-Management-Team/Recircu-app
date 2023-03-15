package com.godzuche.recircu.domain.location

import com.godzuche.recircu.data.location.LocationResult
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getLocation(): Flow<LocationResult>

    //    fun getLocationUpdates(interval: Long): Flow<Location>
    class LocationException(message: String) : Exception()
}