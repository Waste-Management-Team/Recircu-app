package com.godzuche.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.godzuche.recircu.RecircuBottomSheetContent
import com.godzuche.recircu.feature.seller.order.presentation.OrderDetailsRoute
import com.google.accompanist.navigation.animation.composable

const val orderDetailsRoute = "order_details_route"

fun NavController.navigateToOrderDetails(navOptions: NavOptions? = null) {
    this.navigate(orderDetailsRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.orderDetailsScreen(
    showScheduleBottomSheet: (RecircuBottomSheetContent) -> Unit
) {
    composable(orderDetailsRoute) {
        OrderDetailsRoute(
            showScheduleBottomSheet = showScheduleBottomSheet
        )
    }
}