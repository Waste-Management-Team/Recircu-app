package com.example.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.example.recircu.features.seller.waste_listing.WasteDetailsRoute
import com.google.accompanist.navigation.animation.composable

const val wasteDetailsRoute = "waste_details_route"

fun NavController.navigateToWasteDetails(navOptions: NavOptions? = null) {
    this.navigate(wasteDetailsRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.wasteDetailsScreen() {
    composable(wasteDetailsRoute) {
        WasteDetailsRoute()
    }
}