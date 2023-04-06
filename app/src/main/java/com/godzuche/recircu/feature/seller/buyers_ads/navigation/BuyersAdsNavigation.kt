package com.godzuche.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.godzuche.recircu.feature.seller.buyers_ads.BuyersAdsRoute
import com.google.accompanist.navigation.animation.composable

const val buyersAdsRoute = "buyers_ads_route"

fun NavController.navigateToBuyersAds(navOptions: NavOptions? = null) {
    this.navigate(buyersAdsRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.buyersAdsScreen(
    navigateUp: () -> Unit,
    requestFineLocationPermission: () -> Unit
) {
    composable(route = buyersAdsRoute) {
        BuyersAdsRoute(
            navigateUp = navigateUp,
            requestFineLocationPermission = requestFineLocationPermission
        )
    }
}