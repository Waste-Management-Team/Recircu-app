package com.godzuche.recircu.features.google_maps

import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType

data class MapsState(
    val properties: MapProperties = MapProperties(
        isMyLocationEnabled = true
    ),
    val isFalloutMap: Boolean = false
)
