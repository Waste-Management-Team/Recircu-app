package com.example.recircu.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTextField(
    label: String,
    value: String,
    onTextChange: (String) -> Unit
) {
    Column {
        Text(
            label,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.paddingFromBaseline(bottom = 12.dp)
        )
        TextField(
            value = value,
            onValueChange = {
                onTextChange.invoke(it)

            },
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(0.dp, Color.Transparent)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFD9D9D9),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RectangleShape
        )
    }
}