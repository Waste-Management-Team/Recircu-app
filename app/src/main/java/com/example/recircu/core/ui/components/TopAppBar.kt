package com.example.recircu.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import com.example.recircu.R
import com.example.recircu.core.ui.shimmerEffect
import com.example.recircu.features.seller.seller_dashboard.UserState
import com.example.recircu.navigation.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior?,
    userState: UserState
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        when (userState) {
            is UserState.Loading -> {
                Column {
                    Box(
                        modifier = Modifier
                            .width(65.dp)
                            .height(with(LocalDensity.current) { 17.sp.toDp() })
                            .shimmerEffect()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Box(
                        modifier = Modifier
                            .width(218.dp)
                            .height(with(LocalDensity.current) { 34.sp.toDp() })
                            .shimmerEffect()
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Box(
                        modifier = Modifier
                            .width(with(LocalDensity.current) { 157.sp.toDp() })
                            .height(with(LocalDensity.current) { 12.sp.toDp() })
                            .shimmerEffect()
                    )
                }
            }
            is UserState.Success -> {
                Column {
                    Text(
                        text = "Hi ${userState.user.lastName}!",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.paddingFromBaseline(bottom = 14.dp)
                    )
                    Text(
                        stringResource(R.string.welcome_back),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.paddingFromBaseline(bottom = 5.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.location),
                            contentDescription = null,
                            modifier = Modifier.size(17.dp)
                        )
                        Text(
                            userState.user.location,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF00821D)
                        )
                    }
                }
            }
            else -> Unit
        }
        FilledIconButton(
            onClick = {},
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = Color(0xFFE2FFE9)
            ),
            modifier = Modifier.size(45.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                modifier = Modifier
                    .width(18.dp)
                    .height(24.dp),
                contentDescription = null,
                tint = Color(0xFF00821D)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecircuTopBar(
    currentDestination: NavDestination?,
    topLevelDestination: RecircuTopLevelDestination?,
    scrollBehavior: TopAppBarScrollBehavior?,
    navigateUp: () -> Unit
) {
    if (topLevelDestination != null) {
        if (topLevelDestination == RecircuTopLevelDestination.SELLER_HOME) {
        } else {
            MediumTopAppBar(
                title = { Text(stringResource(topLevelDestination.titleTextId!!)) }
            )
        }
    } else {
        currentDestination?.route?.let {
            if (it == wasteTypeRoute) {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = navigateUp) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )
            } else {
                MediumTopAppBar(
                    title = {
                        Text(
                            stringResource(it.toTitleTextId() ?: R.string.empty_string),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = navigateUp) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        }
    }
}

fun String.toTitleTextId(): Int? {
    if (this == gettingStartedRoute) return null
    return when (this) {
        wasteTypeRoute -> R.string.empty_string
        wasteDetailsRoute -> R.string.waste_detail
        else -> R.string.empty_string
    }
}