package com.godzuche.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.godzuche.recircu.feature.seller.seller_dashboard.presentation.BuyerAd
import com.godzuche.recircu.feature.seller.seller_dashboard.presentation.SellerHomeRoute
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

const val sellerHomeRoute = "seller_home_route"
const val sellerHomeGraph = "seller_home_graph"

fun NavController.navigateToSellerGraph(navOptions: NavOptions? = null) {
    this.navigate(sellerHomeGraph, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.sellerHomeGraph(
    navigateToWasteTypes: () -> Unit,
    navigateToBuyer: (BuyerAd) -> Unit,
    navigateToBuyersAds: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    navigation(
        route = sellerHomeGraph,
        startDestination = sellerHomeRoute
    ) {
        composable(sellerHomeRoute) {
            SellerHomeRoute(
                navigateToBuyer = navigateToBuyer,
                navigateToBuyersAds = navigateToBuyersAds
            )
        }
        nestedGraphs()
    }
}