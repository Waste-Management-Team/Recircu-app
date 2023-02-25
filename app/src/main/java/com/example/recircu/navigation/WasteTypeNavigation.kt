package com.example.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.example.recircu.features.seller.seller_dashboard.WasteType
import com.example.recircu.features.seller.waste_listing.WasteTypeRoute
import com.google.accompanist.navigation.animation.composable

const val wasteTypeRoute = "waste_type_route"

fun NavController.navigateToWasteType(navOptions: NavOptions? = null) {
    this.navigate(wasteTypeRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.wasteTypeScreen(
    navigateToWasteDetails: (WasteType) -> Unit
) {
    composable(wasteTypeRoute) {
        WasteTypeRoute(navigateToWasteDetails = navigateToWasteDetails)
    }
}