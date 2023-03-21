package com.godzuche.recircu.core.ui.components

import android.view.MotionEvent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RecircuRatingBar(
    rating: Int = 4,
    onRatingChange: (Int) -> Unit,
    enabled: Boolean
) {
    var selected by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(
        targetValue = if (selected) 40.dp else 24.dp
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        (1..5).forEach { index ->
            Icon(
                imageVector = Icons.Filled.StarRate,
                contentDescription = null,
                modifier = Modifier
                    .size(size)
                    .pointerInteropFilter {
                        if (!enabled) return@pointerInteropFilter true

                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                onRatingChange.invoke(index)
                            }
                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if (index <= rating) {
                    Color.Yellow
                } else {
                    Color.Unspecified
                }
            )
        }
    }
}