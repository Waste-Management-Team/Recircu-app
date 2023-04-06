package com.godzuche.recircu.feature.google_maps.presentation

import android.location.Location
import com.google.maps.android.compose.MapProperties

data class MapsState(
    val lastLocation: Location? = null,
    val properties: MapProperties = MapProperties(
        isMyLocationEnabled = true
    ),
    val isFalloutMap: Boolean = false,
    val isLocationPermissionEnabled: Boolean = true
)
