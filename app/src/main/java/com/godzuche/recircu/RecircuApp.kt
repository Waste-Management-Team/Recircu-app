package com.godzuche.recircu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.godzuche.recircu.core.ui.components.RecircuNavigationDefaults
import com.godzuche.recircu.core.ui.components.RecircuNavigationItem
import com.godzuche.recircu.core.ui.components.RecircuTopBar
import com.godzuche.recircu.core.ui.icon.RecircuIcon
import com.godzuche.recircu.features.google_maps.MapsRoute
import com.godzuche.recircu.features.seller.schedule.ScheduleBottomSheetContent
import com.godzuche.recircu.navigation.RecircuNavHost
import com.godzuche.recircu.navigation.RecircuTopLevelDestination
import com.godzuche.recircu.navigation.wasteTypeRoute
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalMaterialApi::class
)
@Composable
fun RecircuApp(
    onDisplayEdgeToEdgeImmersive: @Composable (Boolean) -> Unit,
    appState: RecircuAppState = rememberRecircuAppState()
) {
    appState.shouldDisplayEdgeToEdge?.let { onDisplayEdgeToEdgeImmersive.invoke(it) }

    val coroutineScope = appState.coroutineScope
    var bottomSheetContent: RecircuBottomSheetContent? by remember {
        mutableStateOf(null)
    }
    val bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    BottomSheetScaffold(
        sheetContent = {
            when (bottomSheetContent) {
                RecircuBottomSheetContent.SCHEDULE -> {
                    ScheduleBottomSheetContent(
                        closeScheduleBottomSheet = {
                            coroutineScope.launch { bottomSheetState.collapse() }
                            bottomSheetContent = null
                        }
                    )
                }
                RecircuBottomSheetContent.MAP -> {
                    MapsRoute(
                        navigateBack = {
                            coroutineScope.launch { bottomSheetState.collapse() }
                            bottomSheetContent = null
                        }
                    )
                }
                null -> {
                    EmptySheet()
                }
            }
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetElevation = 32.dp,
        sheetGesturesEnabled = false
    ) {
        Scaffold(
//        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                if (appState.shouldShowTopAppBar) {
                    RecircuTopBar(
                        currentDestination = appState.currentDestination,
                        topLevelDestination = appState.currentRecircuTopLevelDestination,
                        scrollBehavior = null,
                        navigateUp = { appState.onBackClick() }
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
                    FloatingActionButton(
                        onClick = {
                            appState.navigateToRoute(wasteTypeRoute)
                        },
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
                RecircuNavHost(
                    navController = appState.navController,
                    showScheduleBottomSheet = { sheetContent ->
                        bottomSheetContent = sheetContent
                        coroutineScope.launch {
                            bottomSheetState.expand()
                        }
                    }
                )
                // hide the bottom sheet on back press
                BackHandler(enabled = bottomSheetState.isExpanded) {
                    coroutineScope.launch {
                        bottomSheetState.collapse()
                    }
                    bottomSheetContent = null
                }
            }
        }
    }
//    }
}

@Composable
fun EmptySheet() {
    Box(modifier = Modifier.height(4.dp))
}

@Composable
fun RecircuBottomBar(
    destinations: List<RecircuTopLevelDestination>,
    currentDestination: NavDestination?,
    onNavigateToDestination: (RecircuTopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        contentColor = RecircuNavigationDefaults.navigationContentColor()
//             tonalElevation = 0.dp
    ) {
        destinations.forEach { destination ->
            val selected =
                currentDestination.isBottomBarDestinationInHierarchy(bottomBarDestination = destination)
            RecircuNavigationItem(
                selected = selected,
                onClick = { onNavigateToDestination.invoke(destination) },
                icon = {
                    val icon =
                        if (selected) destination.selectedIcon else destination.unselectedIcon
                    when (icon) {
                        is RecircuIcon.ImageVectorIcon -> {
                            Icon(
                                imageVector = icon.imageVector,
                                contentDescription = stringResource(destination.iconTextId!!)
                            )
                        }
                        is RecircuIcon.PainterResourceIcon -> {
                            Icon(
                                painter = painterResource(icon.id),
                                contentDescription = stringResource(destination.iconTextId!!)
                            )
                        }
                        else -> {}
                    }
                },
                alwaysShowLabel = false
            )
        }
    }
}

fun NavDestination?.isBottomBarDestinationInHierarchy(bottomBarDestination: RecircuTopLevelDestination) =
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