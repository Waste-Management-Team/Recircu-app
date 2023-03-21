package com.godzuche.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.godzuche.recircu.features.seller.seller_dashboard.BuyerDetailsRoute
import com.google.accompanist.navigation.animation.composable

const val buyerDetailsRoute = "buyer_details_route"

fun NavController.navigateToBuyer(navOptions: NavOptions? = null) {
    this.navigate(buyerDetailsRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.buyerDetailsScreen() {
    composable(route = buyerDetailsRoute) {
        BuyerDetailsRoute()
    }
}