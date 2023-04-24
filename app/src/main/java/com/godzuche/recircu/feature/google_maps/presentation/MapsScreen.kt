package com.godzuche.recircu.feature.google_maps.presentation

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
import com.godzuche.recircu.core.designsystem.icon.RecircuIcons
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapsRoute(
    navigateBack: () -> Unit,
    requestFineLocationPermission: () -> Unit,
    mapsViewModel: MapsViewModel = hiltViewModel()
) {
//    viewModel.getLastLocation()
    val mapState by mapsViewModel.state.collectAsStateWithLifecycle()

    MapsScreen(
        mapsState = mapState,
        navigateBack = navigateBack,
        requestFineLocationPermission = requestFineLocationPermission
    )
}

@Composable
fun MapsScreen(
    mapsState: MapsState,
    navigateBack: () -> Unit,
    requestFineLocationPermission: () -> Unit
) {
/*
    if (!mapsState.isLocationPermissionEnabled) {
        Log.d("Location", "reqPerms in map screen")
        requestFineLocationPermission.invoke()
    } else {
        Log.d("Location", "reqPerms in map screen else")
    }
*/

    val lastLocation = mapsState.lastLocation?.let {
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
            properties = mapsState.properties,
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