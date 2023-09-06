package com.godzuche.recircu.core.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.*
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.godzuche.recircu.feature.authentication.navigation.userSelectionRoute
import com.godzuche.recircu.feature.onboarding.navigation.gettingStartedRoute
import com.godzuche.recircu.feature.seller.buyers_ads.navigation.buyersAdsRoute
import com.godzuche.recircu.feature.seller.seller_dashboard.navigation.buyerDetailsRoute
import com.godzuche.recircu.feature.seller.seller_dashboard.navigation.navigateToSellerHomeGraph
import com.godzuche.recircu.feature.seller.seller_dashboard.navigation.sellerHomeRoute
import com.godzuche.recircu.feature.seller.seller_profile.navigation.navigateToSellerProfile
import com.godzuche.recircu.feature.seller.seller_profile.navigation.sellerProfileRoute
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
            userSelectionRoute -> RecircuTopLevelDestination.USER_SELECTION
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
        RecircuTopLevelDestination.values().toList().dropLast(2)

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

    fun navigateToRoute(route: String, navOptions: NavOptions? = null) {
        navController.navigate(route, navOptions)
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
                navController.navigateToSellerHomeGraph(topLevelNavOptions)
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

            else -> Unit
        }
    }

    fun onNavigateUp() {
        navController.navigateUp()
    }

    fun onBackClick() {
        navController.popBackStack()
    }

}