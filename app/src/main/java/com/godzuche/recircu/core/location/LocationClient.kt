package com.godzuche.recircu.core.location

import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getLocation(): Flow<LocationResult>

    //    fun getLocationUpdates(interval: Long): Flow<Location>
//    class LocationException(message: String) : Exception()
}