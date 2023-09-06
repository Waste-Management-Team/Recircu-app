package com.godzuche.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.navOptions
import com.godzuche.recircu.AppMainViewModel
import com.godzuche.recircu.RecircuBottomSheetContent
import com.godzuche.recircu.core.ui.RecircuAppState
import com.godzuche.recircu.feature.authentication.navigation.authGraph
import com.godzuche.recircu.feature.authentication.navigation.navigateToAuthGraph
import com.godzuche.recircu.feature.authentication.navigation.navigateToSellerAuth
import com.godzuche.recircu.feature.authentication.navigation.sellerAuthGraph
import com.godzuche.recircu.feature.authentication.navigation.sellerAuthGraphRoute
import com.godzuche.recircu.feature.onboarding.navigation.gettingStartedScreen
import com.godzuche.recircu.feature.seller.buyers_ads.navigation.buyersAdsScreen
import com.godzuche.recircu.feature.seller.buyers_ads.navigation.navigateToBuyersAds
import com.godzuche.recircu.feature.seller.seller_dashboard.navigation.buyerDetailsScreen
import com.godzuche.recircu.feature.seller.seller_dashboard.navigation.navigateToBuyer
import com.godzuche.recircu.feature.seller.seller_dashboard.navigation.navigateToSellerHomeGraph
import com.godzuche.recircu.feature.seller.seller_dashboard.navigation.sellerHomeGraph
import com.godzuche.recircu.feature.seller.seller_profile.navigation.sellerProfileGraph
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.android.gms.auth.api.identity.SignInClient

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RecircuNavHost(
    appState: RecircuAppState,
    oneTapClient: SignInClient,
    appMainViewModel: AppMainViewModel,
    showScheduleBottomSheet: (RecircuBottomSheetContent) -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = sellerAuthGraphRoute
) {
    val navController = appState.navController
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        gettingStartedScreen {
            val navOptions = navOptions {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
            navController.navigateToAuthGraph(navOptions = navOptions)
        }
        authGraph(
            onSellerClick = {
                navController.navigateToSellerAuth()
            },
            nestedGraph = {
                sellerAuthGraph(
                    oneTapClient = oneTapClient,
                    navigateToHome = {
                        // Clear the entire backstack
                        val navOptions = navOptions {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                        navController.navigateToSellerHomeGraph(/*navOptions = navOptions*/)
                    }
                )
            }
        )
        sellerHomeGraph(
            appMainViewModel = appMainViewModel,
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
                    }
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
            navigateToAuthentication = {
                val navOptions = navOptions {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
                navController.navigateToAuthGraph(navOptions = navOptions)
            },
            nestedGraphs = {
                sellerAccountScreen(appMainViewModel = appMainViewModel)
            }
        )
    }
}