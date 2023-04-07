package com.godzuche.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.godzuche.recircu.RecircuBottomSheetContent
import com.godzuche.recircu.feature.authentication.navigation.authGraph
import com.godzuche.recircu.feature.authentication.navigation.navigateToAuth
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RecircuNavHost(
    navController: NavHostController,
    showScheduleBottomSheet: (RecircuBottomSheetContent) -> Unit,
    requestFineLocationPermission: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = gettingStartedRoute
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        gettingStartedScreen(
//            navigateToDashboard = { navController.navigate(sellerHomeRoute) }
            navigateToAuthentication = {
                navController.navigateToAuth()
            }
        )
        authGraph(
            onSellerClick = {
                navController.navigateToSellerAuth()
            },
            nestedGraph = {
                sellerAuthGraph(
                    navigateToHome = {
                        val navOptions = navOptions {
                            popUpTo(navController.graph.startDestinationId) {
//                                inclusive = true
                            }
                        }
                        navController.navigate(sellerHomeRoute, navOptions)
                    }
                )
            }
        )
        sellerHomeGraph(
            navigateToWasteTypes = {
                navController.navigateToWasteType()
            },
            navigateToBuyer = {
                navController.navigateToBuyer()
            },
            navigateToBuyersAds = {
                navController.navigateToBuyersAds()
            },
            nestedGraphs = {
                buyerDetailsScreen(
                    onBookBuyerClick = {
                        navController.navigateToOrderDetails()
                    }
                )
                buyersAdsScreen(
                    navigateUp = {
                        navController.popBackStack()
                    },
                    requestFineLocationPermission = requestFineLocationPermission
                )
                wasteTypeScreen(
                    navigateToWasteDetails = { navController.navigateToOrderDetails() }
                )
                orderDetailsScreen(
                    showScheduleBottomSheet = showScheduleBottomSheet
                )
            }
        )
        sellerExploreScreen()
        connectScreen()
        sellerProfileGraph(
            nestedGraphs = {
                sellerAccountScreen()
            }
        )
    }
}