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
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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

    private val viewModel: AppMainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
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

        WindowCompat.setDecorFitsSystemWindows(window, false)

        @RequiresApi(Build.VERSION_CODES.P)
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES

        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()

            RecircuTheme {
                val dialogQueue = viewModel.visiblePermissionDialogQueue
                val multiplePermissionsResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { perms ->
                    perms.keys.forEach { permission ->
                        viewModel.onPermissionResult(
                            permission = permission,
                            isGranted = perms[permission] == true
                        )
                    }
                }
                if (uiState is MainActivityUiState.Success) {
                    RecircuApp(
                        startDestination = (uiState as MainActivityUiState.Success)
                            .getStartDestination(auth),
                        openLocationSettings = ::openLocationSettings,
                        requestFineLocationPermission = {
                            Log.d("Location", "reqPerms")
                            // request permissions yet to be granted
                            multiplePermissionsResultLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                )
                            )
                        },
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
                                    systemUiController.systemBarsDarkContentEnabled = useDarkIcons
                                    onDispose { }
                                } else {
                                    systemUiController.isStatusBarVisible = true
                                    systemUiController.systemBarsBehavior =
                                        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

                                    systemUiController.setSystemBarsColor(
                                        color = Color.Transparent,
                                        darkIcons = useDarkIcons
                                    )
                                    systemUiController.systemBarsDarkContentEnabled = useDarkIcons
                                    onDispose {}
                                }
                            }
                        }
                    )
                }
                dialogQueue
                    .reversed()
                    .forEach { permission ->
                        PermissionDialog(
                            permissionTextProvider = when (permission) {
                                Manifest.permission.ACCESS_FINE_LOCATION -> {
                                    FineLocationPermissionTextProvider()
                                }
                                else -> return@forEach
                            },
                            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                                permission
                            ),
                            onDismiss = viewModel::dismissPermissionDialog,
                            onOkClicked = {
                                viewModel.dismissPermissionDialog()
                                multiplePermissionsResultLauncher.launch(
                                    arrayOf(permission)
                                )
                            },
                            onGoToAppSettingsClick = ::openAppSettings
                        )
                    }
                // Try to request permission once at the start of the app
                LaunchedEffect(true) {
                    multiplePermissionsResultLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
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