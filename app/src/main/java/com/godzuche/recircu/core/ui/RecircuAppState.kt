package com.godzuche.recircu.core.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.*
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.godzuche.recircu.navigation.*
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberRecircuAppState(
//    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberAnimatedNavController()
): RecircuAppState {
    return remember(navController, coroutineScope) {
        RecircuAppState(navController, coroutineScope)
    }
}

@Stable
class RecircuAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
//    networkMonitor: NetworkMonitor
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentRecircuTopLevelDestination: RecircuTopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            sellerHomeRoute -> RecircuTopLevelDestination.SELLER_HOME
            sellerExploreRoute -> RecircuTopLevelDestination.EXPLORE
            connectRoute -> RecircuTopLevelDestination.CONNECT
            sellerProfileRoute -> RecircuTopLevelDestination.PROFILE
            gettingStartedRoute -> RecircuTopLevelDestination.GETTING_STARTED
            else -> null
        }

    val shouldDisplayEdgeToEdge: Boolean?
        @Composable get() = currentDestination?.route?.let { it == gettingStartedRoute }

/*    var shouldShowBottomSheet = MutableStateFlow(false)
    var bottomSheetContent: MutableStateFlow<RecircuBottomSheetContent?> =
        MutableStateFlow(null)*/

    val shouldShowTopAppBar
        @Composable get() = shouldDisplayEdgeToEdge == false

    val shouldShowFloatingActionButton: Boolean
        @Composable get() = currentDestination?.route == sellerHomeRoute

    val recircuTopLevelDestinations: List<RecircuTopLevelDestination> =
        RecircuTopLevelDestination.values().toList()

    val sellerBottomBarDestinations: List<RecircuTopLevelDestination> =
        RecircuTopLevelDestination.values().toList().dropLast(1)

    /* var shouldShowDialog by mutableStateOf(false)
         private set*/

    private val bottomBarEnabledRoutes = listOf(
        sellerHomeRoute,
        sellerExploreRoute,
        connectRoute,
        sellerProfileRoute,
        sellerAccountRoute,
        buyersAdsRoute,
        buyerDetailsRoute,
        wasteTypeRoute,
        orderDetailsRoute
    )
    val shouldShowBottomBar: Boolean
        @Composable get() = currentDestination?.route in bottomBarEnabledRoutes

/*    var currentDialog by mutableStateOf<RecircuDialog?>(null)

    fun setShowDialog(shouldShow: Boolean, uiEvent: AppUiEvent.ShowDialog? = null) {
        shouldShowDialog = shouldShow
        currentDialog = uiEvent?.dialog
    }*/

    fun navigateToRoute(route: String) {
        navController.navigate(route)
    }

    fun navigationToSellerBottomBarTopLevelDestination(recircuTopLevelDestination: RecircuTopLevelDestination) {
        val topLevelNavOptions = navOptions {
            launchSingleTop = true
            restoreState = true
            popUpTo(sellerHomeRoute) {
                saveState = true
            }
        }
        when (recircuTopLevelDestination) {
            RecircuTopLevelDestination.SELLER_HOME -> {
                navController.navigateToSellerGraph(topLevelNavOptions)
            }
            RecircuTopLevelDestination.EXPLORE -> {
                navController.navigateToSellerExplore(topLevelNavOptions)
            }
            RecircuTopLevelDestination.CONNECT -> {
                navController.navigateToCommunity(topLevelNavOptions)
            }
            RecircuTopLevelDestination.PROFILE -> {
                navController.navigateToSellerProfile(topLevelNavOptions)
            }
            else -> {}
        }
    }

    fun onNavigateUp() {
        navController.navigateUp()
    }

    fun onBackClick() {
        navController.popBackStack()
    }

}