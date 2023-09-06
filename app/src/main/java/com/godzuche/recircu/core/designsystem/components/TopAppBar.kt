package com.godzuche.recircu.core.designsystem.components

import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import com.godzuche.recircu.R
import com.godzuche.recircu.UserAuthState
import com.godzuche.recircu.core.designsystem.icon.RecircuIcons
import com.godzuche.recircu.core.ui.shimmerEffect
import com.godzuche.recircu.feature.onboarding.navigation.gettingStartedRoute
import com.godzuche.recircu.feature.seller.buyers_ads.navigation.buyersAdsRoute
import com.godzuche.recircu.feature.seller.seller_dashboard.navigation.buyerDetailsRoute
import com.godzuche.recircu.navigation.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    userState: UserAuthState,
    location: Location?,
    scrollBehavior: TopAppBarScrollBehavior?
) {
    Surface {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            when (userState) {
                is UserAuthState.Loading -> {
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

                is UserAuthState.SignedIn -> {
                    Column {
                        Text(
                            text = "Hi ${userState.userData.displayName ?: "No name"}",
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
                            Icon(
                                painter = painterResource(RecircuIcons.Location),
                                contentDescription = null,
                                modifier = Modifier.size(17.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = if (location == null) {
                                    Log.d("TopBar", "Location = null")
                                    "Unknown"
                                } else {
                                    Log.d("TopBar", "Location != null")
                                    "Rivers State University"
                                },
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                is UserAuthState.NotSignedIn -> {}
                is UserAuthState.Error -> {}
                else -> Unit
            }
            FilledTonalIconButton(
                onClick = {}
            ) {
                Icon(
                    painterResource(RecircuIcons.Notifications),
                    contentDescription = stringResource(R.string.notifications)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecircuTopBar(
    currentDestination: NavDestination?,
    topLevelDestination: RecircuTopLevelDestination?,
    scrollBehavior: TopAppBarScrollBehavior?,
    navigateUp: () -> Unit,
    navigateToAccount: () -> Unit
) {
    if (topLevelDestination != null) {
        val shouldShowTopBar =
            topLevelDestination != RecircuTopLevelDestination.SELLER_HOME &&
                    topLevelDestination != RecircuTopLevelDestination.USER_SELECTION
        if (shouldShowTopBar) {
            MediumTopAppBar(
                title = { Text(stringResource(topLevelDestination.titleTextId!!)) },
                scrollBehavior = scrollBehavior,
                actions = {
                    when (topLevelDestination) {
                        RecircuTopLevelDestination.PROFILE -> {
                            IconButton(
                                onClick = navigateToAccount
                            ) {
                                Icon(
                                    imageVector = RecircuIcons.Edit,
                                    contentDescription = null
                                )
                            }
                        }

                        else -> Unit
                    }
                }
            )
        } else Unit
    } else {
        currentDestination?.route?.let { destination ->
            when (destination) {
                wasteTypeRoute -> {
                    TopAppBar(
                        title = {
                            Text(
                                stringResource(
                                    destination.toTitleTextId() ?: R.string.empty_string
                                ),
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
                        },
                        scrollBehavior = scrollBehavior
                    )
                }

                buyersAdsRoute -> Unit
                else -> {
                    MediumTopAppBar(
                        title = {
                            Text(
                                stringResource(
                                    destination.toTitleTextId() ?: R.string.empty_string
                                ),
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
                        },
                        scrollBehavior = scrollBehavior
                    )
                }
            }
        }
    }
}

fun String.toTitleTextId(): Int? {
    if (this == gettingStartedRoute) return null
    return when (this) {
        wasteTypeRoute -> R.string.empty_string
        orderDetailsRoute -> R.string.waste_detail
        buyerDetailsRoute -> R.string.buyer
        sellerAccountRoute -> R.string.account
        else -> R.string.empty_string
    }
}