package com.godzuche.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.godzuche.recircu.AppMainViewModel
import com.godzuche.recircu.RecircuBottomSheetContent
import com.godzuche.recircu.core.firebase.GoogleAuthUiClient
import com.godzuche.recircu.feature.authentication.navigation.authGraph
import com.godzuche.recircu.feature.authentication.navigation.navigateToAuth
import com.godzuche.recircu.feature.seller.buyers_ads.navigation.buyersAdsScreen
import com.godzuche.recircu.feature.seller.buyers_ads.navigation.navigateToBuyersAds
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RecircuNavHost(
    navController: NavHostController,
    showScheduleBottomSheet: (RecircuBottomSheetContent) -> Unit,
    requestFineLocationPermission: () -> Unit,
    googleAuthUiClient: GoogleAuthUiClient,
    appMainViewModel: AppMainViewModel,
    modifier: Modifier = Modifier,
    startDestination: String = sellerAuthGraphRoute
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        gettingStartedScreen(
            navigateToAuthentication = {
                appMainViewModel.saveOnboardingState(isCompleted = true)
                navController.navigateToAuth()
            }
        )
        authGraph(
            onSellerClick = {
                navController.navigateToSellerAuth()
            },
            nestedGraph = {
                sellerAuthGraph(
                    googleAuthUiClient = googleAuthUiClient,
                    navigateToHome = {
                        val navOptions = navOptions {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
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
                    appMainViewModel = appMainViewModel,
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
            appMainViewModel = appMainViewModel,
            googleAuthUiClient = googleAuthUiClient,
            navigateToAuthentication = {
                val navOptions = navOptions {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
                navController.navigateToAuth(navOptions = navOptions)
            },
            nestedGraphs = {
                sellerAccountScreen()
            }
        )
    }
}