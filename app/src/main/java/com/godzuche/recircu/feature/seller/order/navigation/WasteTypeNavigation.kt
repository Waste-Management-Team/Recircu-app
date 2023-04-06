package com.godzuche.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.godzuche.recircu.feature.seller.order.WasteTypeRoute
import com.godzuche.recircu.feature.seller.seller_dashboard.presentation.WasteType
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