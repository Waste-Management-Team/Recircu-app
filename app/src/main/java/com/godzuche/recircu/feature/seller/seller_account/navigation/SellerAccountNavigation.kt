package com.godzuche.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.godzuche.recircu.feature.seller.seller_account.SellerAccountRoute
import com.google.accompanist.navigation.animation.composable

const val sellerAccountRoute = "seller_account_route"

fun NavController.navigateToSellerAccount(navOptions: NavOptions? = null) {
    this.navigate(sellerAccountRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.sellerAccountScreen() {
    composable(route = sellerAccountRoute) {
        SellerAccountRoute()
    }
}