package com.example.recircu

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.example.recircu.navigation.*
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

    val currentSellerBottomBarDestination: SellerBottomBarDestination?
        @Composable get() = when (currentDestination?.route) {
            sellerHomeRoute -> SellerBottomBarDestination.SELLER_HOME
            sellerExploreRoute -> SellerBottomBarDestination.EXPLORE
            sellerProfileRoute -> SellerBottomBarDestination.PROFILE
            else -> null
        }

    val shouldDisplayEdgeToEdge: Boolean?
        @Composable get() = currentDestination?.route?.let { it == gettingStartedRoute }

    val shouldShowTopAppBar
        @Composable get() = shouldDisplayEdgeToEdge == false

    val shouldShowFloatingActionButton: Boolean
        @Composable get() = currentDestination?.route == sellerHomeRoute

    val sellerBottomBarDestinations: List<SellerBottomBarDestination> =
        SellerBottomBarDestination.values().toList()

    private val bottomBarEnabledRoutes = listOf(
        sellerHomeRoute,
        sellerExploreRoute,
        sellerProfileRoute,
        wasteTypeRoute,
        wasteDetailsRoute
    )
    val shouldShowBottomBar: Boolean
        @Composable get() = currentDestination?.route in bottomBarEnabledRoutes

    fun navigateToRoute(route: String) {
        navController.navigate(route)
    }

    fun navigationToSellerBottomBarTopLevelDestination(sellerBottomBarDestination: SellerBottomBarDestination) {
        val topLevelNavOptions = navOptions {
            launchSingleTop = true
            restoreState = true
            popUpTo(sellerHomeRoute) {
                saveState = true
            }
        }
        when (sellerBottomBarDestination) {
            SellerBottomBarDestination.SELLER_HOME -> navController.navigateToSellerGraph(
                topLevelNavOptions
            )
            SellerBottomBarDestination.EXPLORE -> navController.navigateToSellerExplore(
                topLevelNavOptions
            )
            SellerBottomBarDestination.PROFILE -> navController.navigateToSellerProfile(
                topLevelNavOptions
            )
        }
    }
}