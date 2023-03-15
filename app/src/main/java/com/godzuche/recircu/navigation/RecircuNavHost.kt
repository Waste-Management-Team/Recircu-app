package com.godzuche.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.godzuche.recircu.RecircuBottomSheetContent
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RecircuNavHost(
    navController: NavHostController,
    showScheduleBottomSheet: (RecircuBottomSheetContent) -> Unit,
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
                wasteDetailsScreen(
                    showScheduleBottomSheet = showScheduleBottomSheet
                )
            }
        )
        sellerExploreScreen()
        connectScreen()
        sellerProfileScreen()
    }
}