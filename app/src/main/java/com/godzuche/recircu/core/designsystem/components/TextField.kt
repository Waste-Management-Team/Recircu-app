package com.godzuche.recircu.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.godzuche.recircu.core.designsystem.icon.RecircuIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    singleLine: Boolean = false
) {
    val focusManager = LocalFocusManager.current
    val defaultKeyboardActions = KeyboardActions(
        onNext = { focusManager.moveFocus(FocusDirection.Down) },
        onDone = { focusManager.clearFocus() }
    )
    Column(modifier = modifier) {
        Text(
            label,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.paddingFromBaseline(bottom = 12.dp)
        )
        TextField(
            value = value,
            onValueChange = {
                onValueChange.invoke(it)

            },
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(0.dp, Color.Transparent)),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RectangleShape,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            keyboardOptions = keyboardOptions ?: KeyboardOptions.Default,
            keyboardActions = keyboardActions ?: defaultKeyboardActions,
            singleLine = singleLine
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onGetMyLocationFromMap: () -> Unit,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    singleLine: Boolean = false
) {
    val focusManager = LocalFocusManager.current
    val defaultKeyboardActions = KeyboardActions(
        onNext = { focusManager.moveFocus(FocusDirection.Down) },
        onDone = { focusManager.clearFocus() }
    )
    Column(modifier = modifier) {
        Text(
            label,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.paddingFromBaseline(bottom = 12.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(
                MaterialTheme.colorScheme.surfaceVariant,
                shape = RectangleShape
            )
//            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            TextField(
                value = value,
                onValueChange = {
                    onValueChange.invoke(it)

                },
                modifier = Modifier
                    .weight(1f, fill = true)
                    .border(BorderStroke(0.dp, Color.Transparent)),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = RectangleShape,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                keyboardOptions = keyboardOptions ?: KeyboardOptions.Default,
                keyboardActions = keyboardActions ?: defaultKeyboardActions,
                singleLine = singleLine
            )
            IconButton(onClick = onGetMyLocationFromMap) {
                Icon(
                    imageVector = RecircuIcons.MyLocation,
                    contentDescription = null
                )
            }
        }
    }
}