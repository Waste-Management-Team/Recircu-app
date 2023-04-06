package com.godzuche.recircu.feature.seller.schedule

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.godzuche.recircu.core.designsystem.components.RecircuButton
import com.godzuche.recircu.core.designsystem.theme.RecircuTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleBottomSheetContent(
    closeScheduleBottomSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    // clear the autofocus on the time input
    LocalFocusManager.current.clearFocus(force = true)
    val datePickerState = rememberDatePickerState()
    val timeInputState = rememberTimePickerState(is24Hour = false)
    Surface(modifier = modifier) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                DatePicker(
                    state = datePickerState,
                    title = null,
                    showModeToggle = true,
                    colors = DatePickerDefaults.colors(
                        headlineContentColor = MaterialTheme.colorScheme.tertiary,
                        todayDateBorderColor = MaterialTheme.colorScheme.primary,
                        todayContentColor = MaterialTheme.colorScheme.primary,
                        selectedDayContainerColor = MaterialTheme.colorScheme.primary
                    )
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
                TimeInput(
                    state = timeInputState
                )
                Spacer(modifier = Modifier.height(54.dp))
                RecircuButton(
                    onClick = closeScheduleBottomSheet,
                    label = "Done",
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CalPreview() {
    RecircuTheme {
        ScheduleBottomSheetContent(closeScheduleBottomSheet = {})
    }
}