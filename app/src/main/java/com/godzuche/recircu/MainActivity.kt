package com.godzuche.recircu

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.godzuche.recircu.core.designsystem.components.FineLocationPermissionTextProvider
import com.godzuche.recircu.core.designsystem.components.PermissionDialog
import com.godzuche.recircu.core.designsystem.theme.RecircuTheme
import com.godzuche.recircu.core.ui.RecircuApp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var oneTapClient: SignInClient

    private val appMainViewModel: AppMainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                appMainViewModel.uiState
                    .onEach {
                        uiState = it
                    }.collect()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                is MainActivityUiState.Loading -> true
                is MainActivityUiState.Success -> false
            }
        }

        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations
        WindowCompat.setDecorFitsSystemWindows(window, false)

        @RequiresApi(Build.VERSION_CODES.P)
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES

        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()

            RecircuTheme {
                val permissionDialogQueue = appMainViewModel.visiblePermissionDialogQueue
                val multiplePermissionsResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { perms ->
                    perms.keys.forEach { permission ->
                        appMainViewModel.onPermissionResult(
                            permission = permission,
                            isGranted = perms[permission] == true
                        )
                    }
                }
                when (uiState) {
                    is MainActivityUiState.Loading -> Unit
                    is MainActivityUiState.Success -> {
                        if ((uiState as MainActivityUiState.Success).isLocationPermissionGranted.not()) {
                            Log.d(
                                "Permission Dialog",
                                "isLocationPermissionGranted = " +
                                        "${(uiState as MainActivityUiState.Success).isLocationPermissionGranted}"
                            )
                            LaunchedEffect(key1 = (uiState as MainActivityUiState.Success).isLocationPermissionGranted) {
                                multiplePermissionsResultLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                    )
                                )
                            }
                        }
                        RecircuApp(
                            appMainViewModel = appMainViewModel,
                            oneTapClient = oneTapClient,
                            startDestination = (uiState as MainActivityUiState.Success)
                                .getStartDestination(auth),
                            lastLocation = (uiState as MainActivityUiState.Success).lastLocation,
//                            userAuthState = (uiState as MainActivityUiState.Success).userState,
                            onDisplayEdgeToEdgeImmersive = { shouldDisplayEdgeToEdge ->
                                DisposableEffect(
                                    systemUiController,
                                    useDarkIcons,
                                    shouldDisplayEdgeToEdge
                                ) {
                                    if (shouldDisplayEdgeToEdge) {
                                        systemUiController.isStatusBarVisible = false
                                        systemUiController.systemBarsBehavior =
                                            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

                                        systemUiController.setSystemBarsColor(
                                            color = Color.Transparent,
                                            darkIcons = useDarkIcons
                                        )
                                        systemUiController.systemBarsDarkContentEnabled =
                                            useDarkIcons
                                        onDispose { }
                                    } else {
                                        systemUiController.isStatusBarVisible = true
                                        systemUiController.systemBarsBehavior =
                                            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

                                        systemUiController.setSystemBarsColor(
                                            color = Color.Transparent,
                                            darkIcons = useDarkIcons
                                        )
                                        systemUiController.systemBarsDarkContentEnabled =
                                            useDarkIcons
                                        onDispose {}
                                    }
                                }
                            },
                            openLocationSettings = ::openLocationSettings
                        )
                    }
                }
                permissionDialogQueue
                    .reversed()
                    .forEach { permission ->
                        PermissionDialog(
                            permissionTextProvider = when (permission) {
                                Manifest.permission.ACCESS_FINE_LOCATION -> {
                                    FineLocationPermissionTextProvider()
                                }

                                else -> return@forEach
                            },
                            isPermanentlyDeclined = shouldShowRequestPermissionRationale(permission).not(),
                            onDismiss = appMainViewModel::dismissPermissionDialog,
                            onOkClicked = {
                                appMainViewModel.dismissPermissionDialog()
                                multiplePermissionsResultLauncher.launch(
                                    arrayOf(permission)
                                )
                            },
                            onGoToAppSettingsClick = {
                                appMainViewModel.dismissPermissionDialog()
                                openAppSettings()
                            }
                        )
                    }
                // Try to request permission once at the start of the app
                LaunchedEffect(true) {
                    multiplePermissionsResultLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
                    Log.d("Permission Dialog", "LaunchedEffect")
                }
            }
        }
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

fun Activity.openLocationSettings() {
    Intent(
        Settings.ACTION_LOCATION_SOURCE_SETTINGS
    ).also(::startActivity)
}