package com.example.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RecircuNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = gettingStartedRoute
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        gettingStartedScreen(navigateToDashboard = { navController.navigate(sellerHomeRoute) })
        sellerHomeGraph(
            navigateToWasteTypes = { navController.navigateToWasteType() },
            nestedGraphs = {
                wasteTypeScreen(
                    navigateToWasteDetails = { navController.navigateToWasteDetails() }
                )
                wasteDetailsScreen()
            }
        )
        sellerExploreScreen()
        sellerProfileScreen()
    }
}