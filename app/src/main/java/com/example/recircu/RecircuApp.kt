package com.example.recircu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.recircu.core.ui.components.RecircuTopBar
import com.example.recircu.core.ui.icon.RecircuIcon
import com.example.recircu.navigation.RecircuNavHost
import com.example.recircu.navigation.SellerBottomBarDestination
import com.example.recircu.navigation.wasteTypeRoute

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class
)
@Composable
fun RecircuApp(
    onDisplayEdgeToEdgeImmersive: @Composable (Boolean) -> Unit,
    appState: RecircuAppState = rememberRecircuAppState()
) {
    appState.shouldDisplayEdgeToEdge?.let { onDisplayEdgeToEdgeImmersive.invoke(it) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    )
    Scaffold(
//        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            if (appState.shouldShowTopAppBar) {
                RecircuTopBar(
                    currentDestination = appState.currentDestination,
                    currentBottomBarDestination = appState.currentSellerBottomBarDestination,
                    scrollBehavior = scrollBehavior
                )
            }
        },
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                RecircuBottomBar(
                    destinations = appState.sellerBottomBarDestinations,
                    currentDestination = appState.currentDestination,
                    onNavigateToDestination = appState::navigationToSellerBottomBarTopLevelDestination
                )
            }
        },
        floatingActionButton = {
            if (appState.shouldShowFloatingActionButton) {
                SmallFloatingActionButton(
                    onClick = {
                        appState.navigateToRoute(wasteTypeRoute)
                    },
                    containerColor = Color(0xFF00821D),
                    contentColor = Color.White,
                    modifier = Modifier.size(49.dp),
                    shape = CircleShape
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .consumeWindowInsets(it)

        ) {
            RecircuNavHost(navController = appState.navController)
        }
    }
}

@Composable
fun RecircuBottomBar(
    destinations: List<SellerBottomBarDestination>,
    currentDestination: NavDestination?,
    onNavigateToDestination: (SellerBottomBarDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color(0xFFECFFF0),
        contentColor = Color(0xFF00801C),
        tonalElevation = 0.dp
    ) {
        destinations.forEach { destination ->
            val selected =
                currentDestination.isBottomBarDestinationInHierarchy(bottomBarDestination = destination)
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination.invoke(destination) },
                icon = {
                    val icon =
                        if (selected) destination.selectedIcon else destination.unselectedIcon
                    when (icon) {
                        is RecircuIcon.ImageVectorIcon -> {
                            Icon(
                                imageVector = icon.imageVector,
                                contentDescription = stringResource(destination.iconTextId),
//                                tint = Color(0xFF00821D)
                            )
                        }
                        is RecircuIcon.PainterResourceIcon -> {
                            Icon(
                                painter = painterResource(icon.id),
                                contentDescription = stringResource(destination.iconTextId),
//                                tint = Color(0xFF00821D)
                            )
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0xFF97FFAE),
                    unselectedIconColor = Color(0xFF00801C),
                    unselectedTextColor = Color(0xFF00801C),
                    selectedIconColor = Color(0xFF00821D),
                    selectedTextColor = Color(0xFF00821D)
                )
            )
        }
    }
}

fun NavDestination?.isBottomBarDestinationInHierarchy(bottomBarDestination: SellerBottomBarDestination) =
    this?.hierarchy?.any {
        it.route?.contains(bottomBarDestination.name, ignoreCase = true) ?: false
    } ?: false

@Preview
@Composable
fun AppPreview() {
    MaterialTheme {
        RecircuApp(onDisplayEdgeToEdgeImmersive = {})
    }
}