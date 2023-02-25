package com.example.recircu

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.recircu.core.ui.theme.RecircuTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        @RequiresApi(Build.VERSION_CODES.P)
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES

        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()

            RecircuTheme {
                RecircuApp(
                    onDisplayEdgeToEdgeImmersive = { shouldDisplayEdgeToEdge ->


                        if (shouldDisplayEdgeToEdge) {
                            DisposableEffect(
                                systemUiController,
                                useDarkIcons,
                                shouldDisplayEdgeToEdge
                            ) {
                                systemUiController.isStatusBarVisible = false
                                systemUiController.systemBarsBehavior =
                                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

                                systemUiController.setSystemBarsColor(
                                    color = Color.Transparent,
                                    darkIcons = useDarkIcons
                                )
                                systemUiController.systemBarsDarkContentEnabled = useDarkIcons
                                onDispose {}
                            }
                        } else {
                            DisposableEffect(systemUiController, useDarkIcons) {
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
        }
    }
}