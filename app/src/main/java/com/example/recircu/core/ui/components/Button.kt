package com.example.recircu.core.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape

@Composable
fun RecircuButton(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0xFF00801C),
    contentColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RectangleShape
    ) {
        Text(text = label)
    }
}