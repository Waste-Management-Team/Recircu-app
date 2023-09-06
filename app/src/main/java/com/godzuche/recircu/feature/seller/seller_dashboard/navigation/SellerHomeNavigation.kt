package com.godzuche.recircu.feature.seller.seller_dashboard.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.godzuche.recircu.AppMainViewModel
import com.godzuche.recircu.feature.seller.seller_dashboard.presentation.BuyerAd
import com.godzuche.recircu.feature.seller.seller_dashboard.presentation.SellerHomeRoute
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

const val sellerHomeRoute = "seller_home_route"
const val sellerHomeGraphRoute = "seller_home_graph"

fun NavController.navigateToSellerHomeGraph(navOptions: NavOptions? = null) {
    this.navigate(sellerHomeGraphRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.sellerHomeGraph(
    appMainViewModel: AppMainViewModel,
    navigateToBuyer: (BuyerAd) -> Unit,
    navigateToBuyersAds: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    navigation(
        route = sellerHomeGraphRoute,
        startDestination = sellerHomeRoute
    ) {
        composable(sellerHomeRoute) {
            SellerHomeRoute(
                appMainViewModel = appMainViewModel,
                navigateToBuyer = navigateToBuyer,
                navigateToBuyersAds = navigateToBuyersAds
            )
        }
        nestedGraphs()
    }
}