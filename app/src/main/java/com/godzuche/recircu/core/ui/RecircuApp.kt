package com.godzuche.recircu

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.godzuche.recircu.core.designsystem.components.RecircuDialog
import com.godzuche.recircu.core.designsystem.components.RecircuNavigationDefaults
import com.godzuche.recircu.core.designsystem.components.RecircuNavigationItem
import com.godzuche.recircu.core.designsystem.components.RecircuTopBar
import com.godzuche.recircu.core.designsystem.icon.RecircuIcon
import com.godzuche.recircu.core.ui.RecircuAppState
import com.godzuche.recircu.core.ui.rememberRecircuAppState
import com.godzuche.recircu.feature.google_maps.presentation.MapsRoute
import com.godzuche.recircu.feature.google_maps.presentation.MapsViewModel
import com.godzuche.recircu.feature.seller.schedule.ScheduleBottomSheetContent
import com.godzuche.recircu.navigation.*
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalMaterialApi::class
)
@Composable
fun RecircuApp(
    onDisplayEdgeToEdgeImmersive: @Composable (Boolean) -> Unit,
    openLocationSettings: () -> Unit,
    requestFineLocationPermission: () -> Unit,
    appState: RecircuAppState = rememberRecircuAppState(),
    mapsViewModel: MapsViewModel = hiltViewModel()
) {
    appState.shouldDisplayEdgeToEdge?.let { onDisplayEdgeToEdgeImmersive.invoke(it) }

    val coroutineScope = appState.coroutineScope
    var bottomSheetContent: RecircuBottomSheetContent? by remember {
        mutableStateOf(null)
    }
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = topAppBarState)
    val bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    val dialogState by mapsViewModel.dialogState.collectAsStateWithLifecycle()
    if (dialogState.shouldShow) {
        Log.d("Location", "dialog")
        dialogState.dialog?.let {
            RecircuDialog(
                recircuDialog = it,
                onDismissRequest = {
//                    appState.setShowDialog(false)
                    mapsViewModel.setDialogState(shouldShow = false)
                },
                onDismiss = {
                    Log.d("Location", "dialog onDismiss")
//                    appState.setShowDialog(false)
                    mapsViewModel.setDialogState(shouldShow = false)
                },
                onConfirm = {
                    mapsViewModel.setDialogState(shouldShow = false)
                    openLocationSettings.invoke()
                }
            )
        }
    }

    BottomSheetScaffold(
        sheetContent = {
            when (bottomSheetContent) {
                RecircuBottomSheetContent.SCHEDULE -> {
                    ScheduleBottomSheetContent(
                        closeScheduleBottomSheet = {
                            // Todo: Show success dialog
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
                        },
                        requestFineLocationPermission = requestFineLocationPermission
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
            modifier = if (
                appState.shouldShowTopAppBar &&
                appState.currentDestination?.route != sellerHomeRoute &&
                appState.currentDestination?.route != buyersAdsRoute
            ) {
                Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            } else {
                Modifier
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                if (appState.shouldShowTopAppBar) {
                    RecircuTopBar(
                        currentDestination = appState.currentDestination,
                        topLevelDestination = appState.currentRecircuTopLevelDestination,
                        scrollBehavior = scrollBehavior,
                        navigateUp = { appState.onBackClick() },
                        navigateToAccount = {
                            appState.navigateToRoute(sellerAccountRoute)
                        }
                    )
                }
            },
            bottomBar = {
                AnimatedVisibility(
                    visible = appState.shouldShowBottomBar,
                    enter = slideInVertically(
                        animationSpec = tween(),
                        initialOffsetY = {
                            it * 2
                        }
                    ),
                    exit = slideOutVertically(
                        animationSpec = tween(),
                        targetOffsetY = {
                            it * 2
                        }
                    )
                ) {
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
                    },
                    requestFineLocationPermission = requestFineLocationPermission
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
        RecircuApp(
            onDisplayEdgeToEdgeImmersive = {},
            openLocationSettings = {},
            requestFineLocationPermission = {})
    }
}