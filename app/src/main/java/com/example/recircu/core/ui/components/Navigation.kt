package com.example.recircu.core.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RowScope.RecircuNavigationItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    selectedIcon: @Composable () -> Unit = icon,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        colors = NavigationBarItemDefaults.colors(
            selectedTextColor = RecircuNavigationDefaults.navigationSelectedItemColor(),
            selectedIconColor = RecircuNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = RecircuNavigationDefaults.navigationContentColor(),
            unselectedIconColor = RecircuNavigationDefaults.navigationContentColor(),
            indicatorColor = RecircuNavigationDefaults.navigationIndicatorColor()
        ),
        label = label,
        alwaysShowLabel = alwaysShowLabel
    )
}

object RecircuNavigationDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}