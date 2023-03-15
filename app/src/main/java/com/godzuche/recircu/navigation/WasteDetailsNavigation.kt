package com.godzuche.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.godzuche.recircu.RecircuBottomSheetContent
import com.godzuche.recircu.features.seller.order.WasteDetailsRoute
import com.google.accompanist.navigation.animation.composable

const val wasteDetailsRoute = "waste_details_route"

fun NavController.navigateToWasteDetails(navOptions: NavOptions? = null) {
    this.navigate(wasteDetailsRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.wasteDetailsScreen(
    showScheduleBottomSheet: (RecircuBottomSheetContent) -> Unit
) {
    composable(wasteDetailsRoute) {
        WasteDetailsRoute(
            showScheduleBottomSheet = showScheduleBottomSheet
        )
    }
}