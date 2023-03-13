package com.godzuche.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.godzuche.recircu.features.seller.seller_explore.SellerExploreRoute
import com.google.accompanist.navigation.animation.composable

const val sellerExploreRoute = "seller_explore_route"

fun NavController.navigateToSellerExplore(navOptions: NavOptions? = null) {
    this.navigate(sellerExploreRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.sellerExploreScreen() {
    composable(sellerExploreRoute) {
        SellerExploreRoute()
    }
}