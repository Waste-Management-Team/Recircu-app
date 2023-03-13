package com.godzuche.recircu.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.godzuche.recircu.core.ui.theme.GradientColors
import com.godzuche.recircu.core.ui.theme.LocalGradientColors

@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    gradientColors: GradientColors = LocalGradientColors.current,
    content: @Composable () -> Unit
) {
    val currentTopColor by rememberUpdatedState(gradientColors.top)
    val currentBottomColor by rememberUpdatedState(gradientColors.bottom)

    Surface(
        modifier = modifier,
        color = if (gradientColors.container == Color.Unspecified) {
            Color.Transparent
        } else {
            // This will cover the image
            gradientColors.container
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .drawWithCache {
                    val gradient = Brush.verticalGradient(
                        0f to currentTopColor,
                        0.47f to currentBottomColor,
                        1f to currentBottomColor,
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )

                    onDrawBehind {
                        drawRect(gradient)
                    }
                },
        ) {
            content()
        }
    }
}