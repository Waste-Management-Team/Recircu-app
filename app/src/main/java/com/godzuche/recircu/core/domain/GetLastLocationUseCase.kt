package com.godzuche.recircu.core.domain

import com.godzuche.recircu.core.location.LocationClient
import com.godzuche.recircu.core.location.LocationResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLastLocationUseCase @Inject constructor(
    private val locationClient: LocationClient
) {
    operator fun invoke(): Flow<LocationResult> {
        return locationClient.getLocation()
    }
}