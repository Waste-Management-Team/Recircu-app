package com.godzuche.recircu.features.google_maps

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.godzuche.recircu.core.ui.icon.RecircuIcons
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapsRoute(
    viewModel: MapsViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    viewModel.getLastLocation()
    val state by viewModel.state.collectAsStateWithLifecycle()
    MapsScreen(
        state = state,
        navigateBack = navigateBack
    )
}

@Composable
fun MapsScreen(
    state: MapsState,
    navigateBack: () -> Unit
) {
    val lastLocation = state.lastLocation?.let {
        LatLng(it.latitude, it.longitude)
    }

    Log.d("Location", "${lastLocation?.latitude}")

    var cameraPositionState = rememberCameraPositionState()

    if (lastLocation != null) {
        cameraPositionState =
            CameraPositionState(
                position = CameraPosition.fromLatLngZoom(lastLocation, 17f)
            )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = state.properties,
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
//                mapToolbarEnabled = true,
                myLocationButtonEnabled = true
            )
        ) {
            if (lastLocation != null) {
                val lastLocationState = MarkerState(position = lastLocation)
                Marker(
                    state = lastLocationState
                )
            }
        }

        /*TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = navigateBack) {
                    Icon(imageVector = RecircuIcons.Clear, contentDescription = "Close")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
            )
        )*/
        IconButton(
            onClick = navigateBack,
            modifier = Modifier.statusBarsPadding()
        ) {
            Icon(
                imageVector = RecircuIcons.Clear,
                contentDescription = "Close"
            )
        }
    }
}