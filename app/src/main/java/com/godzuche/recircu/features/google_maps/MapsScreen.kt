package com.godzuche.recircu.features.google_maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.godzuche.recircu.core.ui.icon.RecircuIcons
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapsRoute(
    viewModel: MapsViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MapsScreen(
        state = state,
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapsScreen(
    state: MapsState,
    navigateBack: () -> Unit
) {
    val cameraPositionState = rememberCameraPositionState()
    Box(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = navigateBack) {
                    Icon(imageVector = RecircuIcons.Clear, contentDescription = "Close")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
            )
        )
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = state.properties,
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                mapToolbarEnabled = true,
                myLocationButtonEnabled = true
            )
        )
    }
}