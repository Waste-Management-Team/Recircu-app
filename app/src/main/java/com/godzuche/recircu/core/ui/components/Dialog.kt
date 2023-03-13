package com.godzuche.recircu.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClicked: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        properties = DialogProperties(),
        title = {
            Text(text = "Permission Required")
        },
        text = {
            Text(
                text = permissionTextProvider.getDescription(
                    isPermanentlyDeclined = isPermanentlyDeclined
                )
            )
        },
        confirmButton = {
            Text(
                text = if (isPermanentlyDeclined) {
                    "Grant Permission"
                } else {
                    "OK"
                },
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (isPermanentlyDeclined) {
                            onGoToAppSettingsClick.invoke()
                        } else {
                            onOkClicked.invoke()
                        }
                    }
                    .padding(16.dp)
            )
        }
    )
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class FineLocationPermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined fine location permission." +
                    "You can go to the app settings to grant it."
        } else {
            "This app needs access to your fine location for proper functioning"
        }
    }
}

@Composable
fun RecircuDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        title = {},
        text = {
            Text(
                text = "For a better experience, turn on device location, which uses Google's location service."
            )
        },
        dismissButton = {
            Text(
                text = "No, thanks",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(16.dp)
            )
        },
        confirmButton = {
            Text(
                text = "OK",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(16.dp)
            )
        }
    )
}