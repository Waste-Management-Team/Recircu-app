package com.godzuche.recircu.core.ui

import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.navOptions
import com.godzuche.recircu.AppMainViewModel
import com.godzuche.recircu.ConfirmActions
import com.godzuche.recircu.ConfirmationDialog
import com.godzuche.recircu.GpsDisabledDialog
import com.godzuche.recircu.RecircuBottomSheetContent
import com.godzuche.recircu.UserAuthState
import com.godzuche.recircu.core.designsystem.components.RecircuDialog
import com.godzuche.recircu.core.designsystem.components.RecircuNavigationDefaults
import com.godzuche.recircu.core.designsystem.components.RecircuNavigationItem
import com.godzuche.recircu.core.designsystem.components.RecircuTopBar
import com.godzuche.recircu.core.designsystem.icon.RecircuIcon
import com.godzuche.recircu.feature.authentication.navigation.navigateToAuthGraph
import com.godzuche.recircu.feature.google_maps.presentation.MapsRoute
import com.godzuche.recircu.feature.seller.buyers_ads.navigation.buyersAdsRoute
import com.godzuche.recircu.feature.seller.schedule.ScheduleBottomSheetContent
import com.godzuche.recircu.feature.seller.seller_dashboard.navigation.sellerHomeRoute
import com.godzuche.recircu.navigation.RecircuNavHost
import com.godzuche.recircu.navigation.RecircuTopLevelDestination
import com.godzuche.recircu.navigation.sellerAccountRoute
import com.godzuche.recircu.navigation.wasteTypeRoute
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalMaterialApi::class
)
@Composable
fun RecircuApp(
    lastLocation: Location?,
    appMainViewModel: AppMainViewModel,
    startDestination: String,
    oneTapClient: SignInClient,
    onDisplayEdgeToEdgeImmersive: @Composable (Boolean) -> Unit,
    openLocationSettings: () -> Unit,
    appState: RecircuAppState = rememberRecircuAppState()
) {
    appState.shouldDisplayEdgeToEdge?.let { onDisplayEdgeToEdgeImmersive.invoke(it) }

    val coroutineScope = appState.coroutineScope
    var bottomSheetContent: RecircuBottomSheetContent? by remember {
        mutableStateOf(null)
    }
    val context = LocalContext.current
    val userAuthState by appMainViewModel.userAuthState.collectAsStateWithLifecycle()

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = topAppBarState)
    val bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    val dialogState by appMainViewModel.dialogState.collectAsStateWithLifecycle()
    if (dialogState.shouldShow) {
        dialogState.dialog?.let {
            RecircuDialog(
                recircuDialog = it,
                onDismissRequest = {
                    appMainViewModel.setDialogState(shouldShow = false)
                },
                onDismiss = {
                    appMainViewModel.setDialogState(shouldShow = false)
                },
                onConfirm = {
                    when (it) {
                        is GpsDisabledDialog -> {
                            appMainViewModel.setDialogState(shouldShow = false)
                            openLocationSettings.invoke()
                        }

                        is ConfirmationDialog -> {
                            when (it.action) {
                                ConfirmActions.SIGN_OUT -> {
                                    appMainViewModel.setDialogState(shouldShow = false)
                                    appMainViewModel.signOut()
                                }
                            }
                        }
                    }
                }
            )
        }
    }

    when (userAuthState) {
        is UserAuthState.SignedOut -> {
            Log.d("Sign Out", "called in app")
            val navOptions = navOptions {
                popUpTo(appState.navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
            appState.navController.navigateToAuthGraph(navOptions = navOptions)
            /*appState.navController.run {
//                val dir = this.findDestination(authGraphRoute)?.id
                val route = (currentDestination ?: graph).route ?: return
                var destId = *//*action.destinationId*//* findDestination(authGraphRoute)?.id
                val dest = *//*graph.findNode(destId)*//* graph.findNode(route)
                if (dest is NavGraph) {
// Action destination is a nested graph, which isn't a real destination.
// The real destination is the start destination of that graph so resolve it.
                    destId = dest.startDestinationId
                }
                if (currentDestination?.id != destId) {
                    navigate(authGraphRoute)
                }
            }*/
        }

        is UserAuthState.Error -> {
            Toast.makeText(
                context,
                (userAuthState as UserAuthState.Error).e?.message,
                Toast.LENGTH_LONG
            ).show()
        }

        else -> Unit
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
                    appMainViewModel.getLastLocation()
                    MapsRoute(
                        lastLocation = lastLocation,
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
//                            it * 2
                            it
                        }
                    ),
                    exit = slideOutVertically(
                        animationSpec = tween(),
                        targetOffsetY = {
//                            it * 2
                            it
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
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)

            ) {
                RecircuNavHost(
                    appState = appState,
                    startDestination = startDestination,
                    appMainViewModel = appMainViewModel,
                    showScheduleBottomSheet = { sheetContent ->
                        bottomSheetContent = sheetContent
                        coroutineScope.launch {
                            bottomSheetState.expand()
                        }
                    },
                    oneTapClient = oneTapClient
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